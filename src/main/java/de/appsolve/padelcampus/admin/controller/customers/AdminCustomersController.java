/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.customers;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.CustomerDAOI;
import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.utils.HtmlResourceUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/customers")
public class AdminCustomersController extends AdminBaseController<Customer> {
    
    @Autowired
    CustomerDAOI customerDAO;
    
    @Autowired
    HtmlResourceUtil htmlResourceUtil;
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") Customer model, HttpServletRequest request, BindingResult result){
        ModelAndView editView = super.postEditView(model, request, result);
        try {
            htmlResourceUtil.updateCss(request.getServletContext(), sessionUtil.getCustomer(request));
        } catch (Exception e){
            result.addError(new ObjectError("*", e.getMessage()));
        }
        return editView;
    }
    
    @Override
    public CustomerDAOI getDAO() {
        return customerDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/customers";
    }
}
