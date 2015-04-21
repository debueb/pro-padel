/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.general;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.constants.Constants;
import de.appsolve.padelcampus.db.dao.FooterLinkDAOI;
import de.appsolve.padelcampus.db.dao.GenericDAOI;
import de.appsolve.padelcampus.db.model.FooterLink;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/general/footerlinks")
public class AdminGeneralFooterLinksController extends AdminSortableController<FooterLink> {
    
    @Autowired
    FooterLinkDAOI footerLinkDAO;
    
    @Autowired
    ServletContext context;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }
    
    @Override
    public GenericDAOI getDAO() {
        return footerLinkDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/general/footerlinks";
    }
    
    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") FooterLink model, HttpServletRequest request, BindingResult result){
        ModelAndView mav = super.postEditView(model, request, result);
        reloadFooterLinks();
        return mav;
    }
    
    @Override
    public ModelAndView postDelete(@PathVariable("id") Long id){
        ModelAndView mav = super.postDelete(id);
        reloadFooterLinks();
        return mav;
    }
    
    @Override
    public void updateSortOrder(@ModelAttribute("Model") FooterLink model, @RequestBody List<Long> orderedIds){
        super.updateSortOrder(model, orderedIds);
        reloadFooterLinks();
    }

    private void reloadFooterLinks() {
        context.setAttribute(Constants.APPLICATION_FOOTER_LINKS, footerLinkDAO.findAll());
    }
}
