/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import de.appsolve.padelcampus.constants.Constants;
import static de.appsolve.padelcampus.constants.Constants.COOKIE_LOGIN_TOKEN;
import static de.appsolve.padelcampus.constants.Constants.MAIL_NOREPLY_SENDER_NAME;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.data.Credentials;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.model.Contact;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.MailUtils;
import de.appsolve.padelcampus.utils.Msg;
import de.appsolve.padelcampus.utils.RequestUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/login")
public class LoginController extends BaseController{
    
    private static final Logger log = Logger.getLogger(LoginController.class);
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Autowired
    EventDAOI eventDAO;
    
    @Autowired
    Msg msg;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @RequestMapping(value={"", "pre-register"})
    public ModelAndView showLogin(HttpServletRequest request){
        ModelAndView mav = getLoginView(new Credentials(), request);
        return mav;
    }
    
    @RequestMapping(method = POST)
    public ModelAndView doLogin(@Valid @ModelAttribute("Model") Credentials credentials, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response){
        ModelAndView loginView = getLoginView(credentials, request);
        try {
            doLogin(credentials, request, response);
        } catch (Exception e){
            bindingResult.addError(new ObjectError("email", e.getMessage()));
            return loginView;
        }
        
        String redirectPath = sessionUtil.getLoginRedirectPath(request) == null ? "/" : sessionUtil.getLoginRedirectPath(request);
        sessionUtil.setLoginRedirectPath(request, null);
        return new ModelAndView("redirect:"+redirectPath);
    }
    
    @RequestMapping(value="pre-register", method = POST)
    public ModelAndView doPreRegister(@Valid @ModelAttribute("Model") Credentials credentials, BindingResult bindingResult, HttpServletRequest request){
        ModelAndView loginView = getLoginView(credentials, request);
        if (bindingResult.hasErrors()){
            return loginView;
        }
        Player player = playerDAO.findByEmail(credentials.getEmail());
        if (player == null){
            player = new Player();
        }
        if (!StringUtils.isEmpty(player.getPasswordHash())){
            bindingResult.addError(new ObjectError("email", msg.get("EmailAlreadyRegistered")));
            return loginView;
        }
        player.setEmail(credentials.getEmail());
        player.setPassword(credentials.getPassword());
        return getRegisterView(player, request);
    }
    
    @RequestMapping(value="register")
    public ModelAndView getRegister(HttpServletRequest request){
        return getRegisterView(new Player(), request);
    }
    
    @RequestMapping(value="register", method = POST)
    public ModelAndView doRegister(@Valid @ModelAttribute("Model") Player player, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response){
        ModelAndView mav = getRegisterView(player, request);
        if (bindingResult.hasErrors()){
            return mav;
        }
        try {
            doRegister(player, request, response);
            
            String redirectPath = sessionUtil.getLoginRedirectPath(request);
            if (!StringUtils.isEmpty(redirectPath)){
                sessionUtil.setLoginRedirectPath(request, null);
                return new ModelAndView("redirect:"+redirectPath);
            }
            return getRegisterSuccessView();
        } catch(Exception e){
            log.warn("Error while registering user", e);
            bindingResult.addError(new ObjectError("id", e.getMessage()));
            return mav;
        }
    }
    
    @RequestMapping(value="confirm/{UUID}")
    public ModelAndView confirmEmail(@PathVariable("UUID") String UUID){
        Player player = playerDAO.findByUUID(UUID);
        player.setVerified(true);
        playerDAO.saveOrUpdate(player);
        ModelAndView mav = new ModelAndView("login/confirm-email");
        return mav;
    }
    
    @RequestMapping(value="forgot-password", method=GET)
    public ModelAndView forgotPassowrd(){
        return getForgotPasswordView(new Credentials());
    }
    
    @RequestMapping(value="forgot-password", method=POST)
    public ModelAndView forgotPassowrd(@Valid @ModelAttribute("Model") Credentials credentials, BindingResult bindingResult, HttpServletRequest request){
        ModelAndView forgotPasswordView = getForgotPasswordView(credentials);
        if (bindingResult.hasErrors()){
            return forgotPasswordView;
        }
        
        if (StringUtils.isEmpty(credentials.getEmail())){
            bindingResult.addError(new ObjectError("email", msg.get("EmailMayNotBeEmpty")));
            return forgotPasswordView;
        }
        
        Player player = playerDAO.findByEmail(credentials.getEmail());
        if (player == null){
            bindingResult.addError(new ObjectError("email", msg.get("EmailAddressNotRegistered")));
            return forgotPasswordView;
        }
        UUID randomUUID = UUID.randomUUID();
        String resetPasswordURL = RequestUtil.getBaseURL(request)+"/login/reset-password/"+randomUUID.toString();
        
        player.setPasswordResetUUID(randomUUID.toString());
        DateTime expiryDate = new DateTime(Constants.DEFAULT_TIMEZONE).plusDays(1);
        player.setPasswordResetExpiryDate(expiryDate);
        playerDAO.saveOrUpdate(player);

        Mail mail = new Mail();
        String hostname = (request.getServerName().equals("localhost") ? "padelcampus-appsolve.rhcloud.com" : request.getServerName());
        mail.setFrom("noreply@"+hostname);
        Contact contact = new Contact();
        contact.setEmailAddress(player.getEmail());
        contact.setEmailDisplayName(player.toString());
        mail.setRecipients(Arrays.asList(new Contact[]{contact}));
        mail.setSubject(msg.get("ForgotPasswordMailSubject"));
        mail.setBody(StringEscapeUtils.unescapeJava(msg.get("ForgotPasswordMailBody", new Object[]{player.toString(), resetPasswordURL, RequestUtil.getBaseURL(request)})));
        try {
            MailUtils.send(mail);
        } catch (MandrillApiError | IOException e) {
            log.warn("Error while sending reset password instructions", e);
            bindingResult.addError(new ObjectError("email", e.toString()));
            return forgotPasswordView;
        }
        return new ModelAndView("login/forgot-password-success", "Email", credentials.getEmail());
    }
    
    @RequestMapping(value="reset-password/{UUID}", method=GET)
    public ModelAndView resetPassword(@PathVariable("UUID") String UUID){
        Player player = playerDAO.findByPasswordResetUUID(UUID);
        
        if (player == null){
            return new ModelAndView("login/reset-password-link-invalid");
        }
       
        Credentials credentials = new Credentials();
        credentials.setEmail(player.getEmail());
        return getResetPasswordView(credentials);
    }
    
    @RequestMapping(value="reset-password/{UUID}", method=POST)
    public ModelAndView resetPassword(@Valid @ModelAttribute("Model") Credentials credentials, BindingResult bindingResult, @PathVariable("UUID") String UUID){
        ModelAndView mav = getResetPasswordView(credentials);
        Player player = playerDAO.findByPasswordResetUUID(UUID);
         
        if (!player.getEmail().equals(credentials.getEmail())){
            bindingResult.addError(new ObjectError("email", "nice try"));
            return mav;
        }
        
        if (player.getPasswordResetExpiryDate().isBeforeNow()){
            bindingResult.addError(new ObjectError("email", msg.get("PasswordResetLinkExpired")));
            return mav;
        }
        
        if (StringUtils.isEmpty(credentials.getPassword())){
            bindingResult.addError(new ObjectError("email", msg.get("PasswordMayNotBeEmpty")));
            return mav;
        }
        
        player.setPasswordHash(getHash(credentials.getPassword()));
        playerDAO.saveOrUpdate(player);
        return new ModelAndView("login/reset-password-success");
    }
    

    private ModelAndView getLoginView(Credentials credentials, HttpServletRequest request) {
        checkForRedirectParam(request);
        ModelAndView mav = new ModelAndView("login/login", "Model", credentials);
        return mav;
    }
    
    private ModelAndView getRegisterView(Player player, HttpServletRequest request) {
        checkForRedirectParam(request);
        ModelAndView mav = new ModelAndView("login/register", "Model", player);
        return mav;
    }

    private ModelAndView getRegisterSuccessView() {
        return new ModelAndView("login/register-success");
    }
    
    private ModelAndView getForgotPasswordView(Credentials credentials) {
        return new ModelAndView("login/forgot-password", "Model", credentials);
    }

    private ModelAndView getResetPasswordView(Credentials credentials) {
         return new ModelAndView("login/reset-password", "Model", credentials);
    }
    
    
     private Player doLogin(Credentials credentials, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtils.isEmpty(credentials.getEmail())){
            throw new Exception(msg.get("EmailMayNotBeEmpty"));
        }
        if (StringUtils.isEmpty(credentials.getPassword())){
            throw new Exception(msg.get("PasswordMayNotBeEmpty"));
        }
        
        Player player = playerDAO.findByEmail(credentials.getEmail());
        String hash = getHash(credentials.getPassword());
        if (player == null || StringUtils.isEmpty(player.getPasswordHash()) || !hash.equals(player.getPasswordHash())){
            throw new Exception(msg.get("UnknownEmailAddressOrPassword"));
        }
        
        setLoginCookieAndAccessLevel(request, response, player);
        sessionUtil.setUser(request, player);
        return player;
    }
    
    private void doRegister(Player player, HttpServletRequest request, HttpServletResponse response) throws Exception{
            if (StringUtils.isEmpty(player.getPassword())){
                throw new Exception(msg.get("PasswordMayNotBeEmpty"));
            }
            
            Player persistedPlayer = playerDAO.findByEmail(player.getEmail());
            if (persistedPlayer!=null && !StringUtils.isEmpty(persistedPlayer.getPasswordHash())){
                throw new Exception(msg.get("EmailAlreadyRegistered"));
            }
            
            if (persistedPlayer!=null){
                //update existing player instead of generating a new one
                player.setId(persistedPlayer.getId());
            }
            
            //create player object which also generates a UUID
            player = playerDAO.createOrUpdate(player);
            
            String confirmRegistrationURL = RequestUtil.getBaseURL(request)+"/login/confirm/"+player.getUUID();
            
            Mail mail = new Mail();
            mail.setFrom(MAIL_NOREPLY_SENDER_NAME+"@"+RequestUtil.getMailHostName(request));
            Contact contact = new Contact();
            contact.setEmailAddress(player.getEmail());
            contact.setEmailDisplayName(player.toString());
            mail.setRecipients(Arrays.asList(new Contact[]{contact}));
            mail.setSubject(msg.get("RegistrationMailSubject"));
            mail.setBody(StringEscapeUtils.unescapeJava(msg.get("RegistrationMailBody", new Object[]{player.toString(), confirmRegistrationURL, RequestUtil.getBaseURL(request)})));
            MailUtils.send(mail);
            
            //make sure to save the password only after the email has been successfully sent
            player.setPasswordHash(getHash(player.getPassword()));
            playerDAO.saveOrUpdate(player);
            
            //set auto login cookie if user has requested so
            setLoginCookieAndAccessLevel(request, response, player);
            
            //login user
            sessionUtil.setUser(request, player);
    }
    
    private void setLoginCookieAndAccessLevel(HttpServletRequest request, HttpServletResponse response, Player player) {
        //set login cookie
        
        String stayLoggedIn = request.getParameter("stay-logged-in");
        Cookie cookie = new Cookie(COOKIE_LOGIN_TOKEN, player.getUUID());
        if (!request.getServerName().equals("localhost")){
            cookie.setDomain(request.getServerName());
        }
        if (!StringUtils.isEmpty(stayLoggedIn) && stayLoggedIn.equals("on")){
            cookie.setMaxAge(Integer.MAX_VALUE);
        } else {
            cookie.setMaxAge(0); //deletes cookie if it exists
        }
        response.addCookie(cookie);
        
        String accessLevel = "loggedIn";
        List<Event> eventsWithParticipant = eventDAO.findAllWithParticipant(player);
        if (!eventsWithParticipant.isEmpty()){
            accessLevel = "loggedInAndParticipant";
        }
        sessionUtil.setAccessLevel(request, accessLevel); 
    }
    
    private String getHash(String input) {
        return DigestUtils.sha512Hex(input);
    }

    private void checkForRedirectParam(HttpServletRequest request) {
        String redirectPath = request.getParameter("redirect");
        if (!StringUtils.isEmpty(redirectPath) && !redirectPath.contains("://")){
            sessionUtil.setLoginRedirectPath(request, redirectPath);
        }
    }
}
