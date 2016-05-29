/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.pro;

import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.constants.Privilege;
import de.appsolve.padelcampus.data.CustomerRegistrationModel;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.AdminGroupDAOI;
import de.appsolve.padelcampus.db.dao.CustomerDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.AdminGroup;
import de.appsolve.padelcampus.db.model.Contact;
import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.exceptions.MailException;
import de.appsolve.padelcampus.external.cloudflare.CloudFlareApiClient;
import de.appsolve.padelcampus.external.openshift.OpenshiftApiClient;
import de.appsolve.padelcampus.utils.HtmlResourceUtil;
import de.appsolve.padelcampus.utils.MailUtils;
import de.appsolve.padelcampus.utils.Msg;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@Controller
@RequestMapping("/pro")
public class ProController {
    
    private static final Logger LOG = Logger.getLogger(ProController.class);
    
    @Autowired
    CustomerDAOI customerDAO;
    
    @Autowired
    PlayerDAOI playerDAO;
    
    @Autowired
    AdminGroupDAOI adminGroupDAO;
    
    @Autowired
    Msg msg;
    
    @Autowired
    CloudFlareApiClient cloudFlareApiClient;
    
    @Autowired
    OpenshiftApiClient openshiftApiClient;
    
    @Autowired
    ServletContext servletContext;
    
    @Autowired
    HtmlResourceUtil htmlResourceUtil;
    
    private final static Pattern DNS_SUBDOMAIN_PATTERN = Pattern.compile("(?:[A-Za-z0-9][A-Za-z0-9\\-]{0,61}[A-Za-z0-9]|[A-Za-z0-9])");
    
    private final static String OPENSHIFT_URL   = "padelcampus-appsolve.rhcloud.com";
    private final static String CLOUDFLARE_URL  = "pro-padel.de";
    
    @RequestMapping
    public ModelAndView index(){
        return new ModelAndView("pro/index");
    }
    
    @RequestMapping(method=GET, value="newaccount")
    public ModelAndView newAccount(){
        CustomerRegistrationModel customerAccount = new CustomerRegistrationModel();
        Customer customer = new Customer();
        customer.setName("Test");
        customer.setDomainNames(Collections.singleton("test"));
        customerAccount.setCustomer(customer);
        Player player = new Player();
        player.setEmail("test@appsolve.de");
        player.setFirstName("CustomerRegistration");
        player.setLastName("Test");
        player.setGender(Gender.male);
        player.setPhone("00491772731231");
        player.setPassword("test");
        customerAccount.setPlayer(player);
        return new ModelAndView("pro/newaccount", "Model", customerAccount);
        //return new ModelAndView("pro/newaccount", "Model", new CustomerRegistrationModel());
    }
    
    @RequestMapping(method=POST, value="newaccount")
    public ModelAndView postNewAccount(@ModelAttribute("Model") CustomerRegistrationModel customerAccount, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ModelAndView("pro/newaccount", "Model", customerAccount);
        }
        
        try {
            //verify dns name requirements
            if (!DNS_SUBDOMAIN_PATTERN.matcher(customerAccount.getCustomer().getName()).matches()){
                throw new Exception(msg.get("ProjectNameFormatRequirements"));
            }
            
            //make sure customer does not exist yet
            Customer customer = customerDAO.findByName(customerAccount.getCustomer().getName().toLowerCase());
            if (customer != null){
                throw new Exception(msg.get("ProjectAlreadyExists"));
            }
            
            //create DNS subdomain in cloudflare
            String domainName = cloudFlareApiClient.addCnameRecord(customerAccount.getCustomer().getName(), CLOUDFLARE_URL, OPENSHIFT_URL);
            
            //create openshift alias
            openshiftApiClient.addAlias(domainName, OPENSHIFT_URL);
            
            //save customer to DB
            HashSet<String> domainNames = new HashSet<>();
            domainNames.add(domainName);
            customerAccount.getCustomer().setDomainNames(domainNames);
            customer = customerDAO.saveOrUpdate(customerAccount.getCustomer());

            //create admin account in DB
            Player adminPlayer = playerDAO.findByEmail(customerAccount.getPlayer().getEmail());
            if (adminPlayer == null){
                customerAccount.getPlayer().setCustomer(customer);
                adminPlayer = playerDAO.saveOrUpdate(customerAccount.getPlayer());
            }
            
            //create admin group in DB
            AdminGroup adminGroup = adminGroupDAO.findByAttribute("customer", customer);
            if (adminGroup == null){
                adminGroup = new AdminGroup();
                adminGroup.setName(domainName + " Admins");
                EnumSet<Privilege> privileges = EnumSet.complementOf(EnumSet.of(Privilege.ManageCustomers));
                adminGroup.setPrivileges(privileges);
                adminGroup.setPlayers(new HashSet<>(Arrays.asList(new Player[]{adminPlayer})));
                adminGroup.setCustomer(customer);
                adminGroupDAO.saveOrUpdate(adminGroup);
            }
            
            //create all.min.css.stylesheet for new customer
            htmlResourceUtil.updateCss(servletContext, customer);
            
            sendMail(customerAccount, "registration successful");
            
            return new ModelAndView("redirect:/pro/newaccount/"+domainName);
        } catch (Exception e){
            LOG.error(e.getMessage(), e);
            sendMail(customerAccount, e.getMessage());
            bindingResult.addError(new ObjectError("id", e.getMessage()));
            return new ModelAndView("pro/newaccount", "Model", customerAccount);
        }
    }
    
    @RequestMapping("newaccount/{domainName}")
    public ModelAndView newAccountSuccess(@PathVariable("domainName") String domainName){
        boolean dnsRecordExists = true;
        try {
            InetAddress.getByName(domainName);
        } catch (UnknownHostException e){
            dnsRecordExists = false;
        }
        ModelAndView mav =  new ModelAndView("pro/newaccount-success");
        mav.addObject("domainName", domainName);
        mav.addObject("dnsRecordExists", dnsRecordExists);
        return mav;
    }

    private void sendMail(CustomerRegistrationModel customerAccount, String message) {
        try {
            Contact contact = new Contact();
            contact.setEmailAddress("d.wisskirchen@gmail.com");
            Mail mail = new Mail();
            mail.addRecipient(contact);
            mail.setFrom("customer-registration@"+CLOUDFLARE_URL);
            mail.setReplyTo("noreply@"+CLOUDFLARE_URL);
            mail.setSubject("Customer Registration");
            StringBuilder body = new StringBuilder();
            body.append("Customer name: ").append(customerAccount.getCustomer().getName()).append("\n");
            body.append("Player name: ").append(customerAccount.getPlayer().toString()).append("\n");
            body.append("Player email: ").append(customerAccount.getPlayer().getEmailAddress()).append("\n");
            body.append("Message: ").append(message);
            mail.setBody(body.toString());
            MailUtils.send(mail);
        } catch (MailException | IOException ex) {
            LOG.error(ex);
        }
    }
}
