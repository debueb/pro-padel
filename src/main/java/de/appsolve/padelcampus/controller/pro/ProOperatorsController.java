/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.pro;

import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.constants.Privilege;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.data.CustomerRegistrationModel;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.AdminGroupDAOI;
import de.appsolve.padelcampus.db.dao.CustomerDAOI;
import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.model.AdminGroup;
import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.external.cloudflare.CloudFlareApiClient;
import de.appsolve.padelcampus.reporting.ErrorReporter;
import de.appsolve.padelcampus.utils.HtmlResourceUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.regex.Pattern;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller
@RequestMapping("/pro/operators")
public class ProOperatorsController extends BaseController implements ServletContextAware {

    private static final Logger LOG = Logger.getLogger(ProOperatorsController.class);
    private final static Pattern DNS_SUBDOMAIN_PATTERN = Pattern.compile("(?:[A-Za-z0-9][A-Za-z0-9\\-]{0,61}[A-Za-z0-9]|[A-Za-z0-9])");
    @Autowired
    CustomerDAOI customerDAO;
    @Autowired
    PlayerDAOI playerDAO;
    @Autowired
    AdminGroupDAOI adminGroupDAO;
    @Autowired
    CloudFlareApiClient cloudFlareApiClient;
    @Autowired
    HtmlResourceUtil htmlResourceUtil;
    @Autowired
    ErrorReporter errorReporter;
    private ServletContext servletContext;

    @Autowired
    Environment environment;

    @RequestMapping()
    public ModelAndView operators() {
        return new ModelAndView("pro/operators", "Customers", customerDAO.findAll());
    }

    @RequestMapping(method = GET, value = "newaccount")
    public ModelAndView newAccount() {
        return new ModelAndView("pro/newaccount", "Model", new CustomerRegistrationModel());
    }

    @RequestMapping(method = POST, value = "newaccount")
    public ModelAndView postNewAccount(HttpServletRequest request, @ModelAttribute("Model") CustomerRegistrationModel customerAccount, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("pro/newaccount", "Model", customerAccount);
        }
        try {
            if (StringUtils.isEmpty(customerAccount.getCustomer().getName())) {
                throw new Exception(msg.get("ProjectNameFormatRequirements"));
            }
            String projectName = customerAccount.getCustomer().getName().toLowerCase(Constants.DEFAULT_LOCALE).replace(" ", "-");
            //verify dns name requirements
            if (!DNS_SUBDOMAIN_PATTERN.matcher(projectName).matches()) {
                throw new Exception(msg.get("ProjectNameFormatRequirements"));
            }

            //make sure customer does not exist yet
            Customer customer = customerDAO.findByName(projectName);
            if (customer != null) {
                throw new Exception(msg.get("ProjectAlreadyExists"));
            }

            String dnsHostname = environment.getProperty("DNS_HOSTNAME");
            String cloudflareUrl = environment.getProperty("CLOUDFLARE_URL");
            //create DNS subdomain in cloudflare
            String domainName = cloudFlareApiClient.addCnameRecord(projectName, cloudflareUrl, dnsHostname);

            //save customer to DB
            HashSet<String> domainNames = new HashSet<>();
            domainNames.add(domainName);
            customerAccount.getCustomer().setDomainNames(domainNames);
            customer = customerDAO.saveOrUpdate(customerAccount.getCustomer());

            //create admin account in DB
            Player adminPlayer = playerDAO.findByEmail(customerAccount.getPlayer().getEmail());
            if (adminPlayer == null) {
                customerAccount.getPlayer().setCustomer(customer);
                adminPlayer = playerDAO.saveOrUpdate(customerAccount.getPlayer());
            }

            //create admin group in DB
            AdminGroup adminGroup = adminGroupDAO.findByAttribute("customer", customer);
            if (adminGroup == null) {
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

            Mail mail = new Mail();
            mail.addRecipient(getDefaultContact());
            mail.setSubject("New Customer Registration");
            mail.setBody(customer.toString());
            mailUtils.send(mail, request);

            return new ModelAndView("redirect:/pro/operators/newaccount/" + customer.getId());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            errorReporter.notify(e);
            bindingResult.addError(new ObjectError("id", e.getMessage()));
            return new ModelAndView("pro/newaccount", "Model", customerAccount);
        }
    }

    @RequestMapping("newaccount/{customerId}")
    public ModelAndView newAccountSuccess(@PathVariable("customerId") Long customerId) {
        Customer customer = customerDAO.findById(customerId);
        String domainName = customer.getDomainNames().iterator().next();

        ModelAndView mav = new ModelAndView("pro/newaccount-success");
        mav.addObject("domainName", domainName);
        return mav;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
