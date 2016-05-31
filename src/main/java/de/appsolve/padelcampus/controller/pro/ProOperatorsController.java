/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.pro;

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
import java.util.Hashtable;
import java.util.regex.Pattern;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.ServletContext;
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
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("/pro/operators")
public class ProOperatorsController {
    
    private static final Logger LOG = Logger.getLogger(ProOperatorsController.class);
    
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
    public ModelAndView postNewAccount(@ModelAttribute("Model") CustomerRegistrationModel customerAccount, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ModelAndView("pro/newaccount", "Model", customerAccount);
        }
        
        try {
            if (StringUtils.isEmpty(customerAccount.getCustomer().getName())){
                throw new Exception(msg.get("ProjectNameFormatRequirements"));
            }
            String projectName = customerAccount.getCustomer().getName().toLowerCase().replace(" ", "-");
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
            
            sendMail(customerAccount, "registration successful");
            
            return new ModelAndView("redirect:/pro/newaccount/"+domainName);
        } catch (Exception e){
            LOG.error(e.getMessage(), e);
            sendMail(customerAccount, e.getMessage());
            bindingResult.addError(new ObjectError("id", e.getMessage()));
            return new ModelAndView("pro/newaccount", "Model", customerAccount);
        }
    }
    
    @RequestMapping("newaccount/{customerId}")
    public ModelAndView newAccountSuccess(@PathVariable("customerId") Long customerId) throws NamingException{
        Customer customer = customerDAO.findById(customerId);
        String domainName = customer.getDomainNames().iterator().next();
        Attribute record = getDnsRecord(domainName);
        
        ModelAndView mav =  new ModelAndView("pro/newaccount-success");
        mav.addObject("domainName", domainName);
        mav.addObject("dnsRecordExists", record!=null);
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

    private Attribute getDnsRecord(String domainName) throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();

        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("com.sun.jndi.dns.timeout.initial", "5000");    /* quite short... too short? */
        env.put("com.sun.jndi.dns.timeout.retries", "1");

        DirContext ictx = new InitialDirContext(env);
        String[] ids = new String[] {"A"};
        try {
            Attributes attrs = ictx.getAttributes(domainName, ids);
            Attribute a = attrs.get("A");
            return a;
        } catch (NameNotFoundException e){
            return null;
        }
    }
}
