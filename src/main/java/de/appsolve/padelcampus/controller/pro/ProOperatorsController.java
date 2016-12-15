/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.pro;

import de.appsolve.padelcampus.constants.Constants;
import static de.appsolve.padelcampus.constants.Constants.CONTACT_FORM_RECIPIENT_MAIL;
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
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("/pro/operators")
public class ProOperatorsController implements ServletContextAware{
    
    private static final Logger LOG = Logger.getLogger(ProOperatorsController.class);
    
    private ServletContext servletContext;
    
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
    HtmlResourceUtil htmlResourceUtil;
    
    @Autowired
    MailUtils mailUtils;
    
    private final static Pattern DNS_SUBDOMAIN_PATTERN = Pattern.compile("(?:[A-Za-z0-9][A-Za-z0-9\\-]{0,61}[A-Za-z0-9]|[A-Za-z0-9])");
    
    private final static String OPENSHIFT_URL   = "padelkoeln-appsolve.rhcloud.com";
    private final static String CLOUDFLARE_URL  = "pro-padel.de";
    
    @RequestMapping()
    public ModelAndView operators(){
        return new ModelAndView("pro/operators", "Customers", customerDAO.findAll());
    }
    
    @RequestMapping(method=GET, value="newaccount")
    public ModelAndView newAccount(){
        return new ModelAndView("pro/newaccount", "Model", new CustomerRegistrationModel());
    }
    
    @RequestMapping(method=POST, value="newaccount")
    public ModelAndView postNewAccount(HttpServletRequest request, @ModelAttribute("Model") CustomerRegistrationModel customerAccount, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ModelAndView("pro/newaccount", "Model", customerAccount);
        }
        try {
            if (StringUtils.isEmpty(customerAccount.getCustomer().getName())){
                throw new Exception(msg.get("ProjectNameFormatRequirements"));
            }
            String projectName = customerAccount.getCustomer().getName().toLowerCase(Constants.DEFAULT_LOCALE).replace(" ", "-");
            //verify dns name requirements
            if (!DNS_SUBDOMAIN_PATTERN.matcher(projectName).matches()){
                throw new Exception(msg.get("ProjectNameFormatRequirements"));
            }
            
            //make sure customer does not exist yet
            Customer customer = customerDAO.findByName(projectName);
            if (customer != null){
                throw new Exception(msg.get("ProjectAlreadyExists"));
            }
            
            //create DNS subdomain in cloudflare
            String domainName = cloudFlareApiClient.addCnameRecord(projectName, CLOUDFLARE_URL, OPENSHIFT_URL);
            
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
            
            sendMail(request, customerAccount, "registration successful");
            
            return new ModelAndView("redirect:/pro/operators/newaccount/"+customer.getId());
        } catch (Exception e){
            LOG.error(e.getMessage(), e);
            sendMail(request, customerAccount, e.getMessage());
            bindingResult.addError(new ObjectError("id", e.getMessage()));
            return new ModelAndView("pro/newaccount", "Model", customerAccount);
        }
    }
    
    @RequestMapping("newaccount/{customerId}")
    public ModelAndView newAccountSuccess(@PathVariable("customerId") Long customerId) {
        Customer customer = customerDAO.findById(customerId);
        String domainName = customer.getDomainNames().iterator().next();
        
        ModelAndView mav =  new ModelAndView("pro/newaccount-success");
        mav.addObject("domainName", domainName);
        return mav;
    }

    private void sendMail(HttpServletRequest request, CustomerRegistrationModel customerAccount, String message) {
        try {
            Contact contact = new Contact();
            contact.setEmailAddress(CONTACT_FORM_RECIPIENT_MAIL);
            Mail mail = new Mail();
            mail.addRecipient(contact);
            mail.setReplyTo("noreply@"+CLOUDFLARE_URL);
            mail.setSubject("Customer Registration");
            StringBuilder body = new StringBuilder();
            body.append("Customer name: ").append(customerAccount.getCustomer().getName()).append("\n");
            body.append("Player name: ").append(customerAccount.getPlayer().toString()).append("\n");
            body.append("Player email: ").append(customerAccount.getPlayer().getEmailAddress()).append("\n");
            body.append("Message: ").append(message);
            mail.setBody(body.toString());
            mailUtils.send(mail, request);
        } catch (MailException | IOException ex) {
            LOG.error(ex);
        }
    }

    //does not work on openshift: permission denied
//    private Attribute getDnsRecord(String domainName) {
//        Hashtable<String, String> env = new Hashtable<>();
//
//        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
//        env.put("com.sun.jndi.dns.timeout.initial", "5000");    /* quite short... too short? */
//        env.put("com.sun.jndi.dns.timeout.retries", "1");
//
//        try {
//            DirContext ictx = new InitialDirContext(env);
//            String[] ids = new String[] {"A"};
//            Attributes attrs = ictx.getAttributes(domainName, ids);
//            Attribute a = attrs.get("A");
//            return a;
//        } catch (NamingException e){
//            LOG.warn(e);
//        }
//        return null;
//    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
