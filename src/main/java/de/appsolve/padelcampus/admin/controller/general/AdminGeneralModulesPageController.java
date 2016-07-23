/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.general;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.dao.PageEntryDAOI;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.PageEntry;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/general/modules/page/{moduleId}")
public class AdminGeneralModulesPageController extends AdminSortableController<PageEntry>{
    
    @Autowired
    ModuleDAOI moduleDAO;
    
    @Autowired
    PageEntryDAOI pageEntryDAO;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }
    
    @Override
    public String getModuleName() {
        return "admin/general/modules/page";
    }
    
    @Override
    public BaseEntityDAOI getDAO() {
        return pageEntryDAO;
    }
    
    @Override
    public ModelAndView showIndex(HttpServletRequest request, Pageable pageable, String search){
        Module module = getModule(request);
        List<PageEntry> all = pageEntryDAO.findByModule(module);
        Collections.sort(all);
        ModelAndView mav = getIndexView(new PageImpl<>(all));
        mav.addObject("Module", module);
        return mav;
    }
    
    @Override 
    public ModelAndView redirectToIndex(HttpServletRequest request){
        return new ModelAndView("redirect:/"+getModuleName()+"/"+getModule(request).getId());
    }

    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") PageEntry model, HttpServletRequest request, BindingResult result){
        Module module = getModule(request);
        if (model.getPosition() == null){
            model.setPosition(0L+pageEntryDAO.findAll().size());
        }
        model.setModule(module);
        return super.postEditView(model, request, result);
    }
    
    @Override
    public ModelAndView postDelete(HttpServletRequest request, @PathVariable("id") Long id){
        try {
            PageEntry news = pageEntryDAO.findById(id);
            news.setModule(null);
            pageEntryDAO.saveOrUpdate(news);
            pageEntryDAO.deleteById(id);
        } catch (DataIntegrityViolationException e){
            PageEntry model = pageEntryDAO.findById(id);
            LOG.warn("Attempt to delete "+model+" failed due to "+e);
            ModelAndView deleteView = getDeleteView(model);
            deleteView.addObject("error", msg.get("CannotDeleteDueToRefrence", new Object[]{model.toString()}));
            return deleteView;
        }
        return redirectToIndex(request);
    }

    private Module getModule(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        int index = requestURI.indexOf(getModuleName()) + getModuleName().length() + 1;
        int toIndex = requestURI.indexOf("/", index);
        toIndex = (toIndex == -1) ? requestURI.length() : toIndex;
        String moduleIdentifier = requestURI.substring(index, toIndex);
        return moduleDAO.findById(Long.parseLong(moduleIdentifier));
    }
}
