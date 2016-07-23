/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.general;

import de.appsolve.padelcampus.constants.EventType;
import de.appsolve.padelcampus.constants.ModuleType;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import de.appsolve.padelcampus.utils.FileUtil;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import de.appsolve.padelcampus.utils.ModuleUtil;
import java.io.IOException;
import java.util.List;
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
@RequestMapping("/admin/general/modules")
public class AdminGeneralModulesController extends AdminSortableController<Module> {
    
    @Autowired
    ModuleDAOI moduleDAO;
    
    @Autowired
    ModuleUtil moduleUtil;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }
    
    @Override
    public BaseEntityDAOI getDAO() {
        return moduleDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/general/modules";
    }
    
    @Override
    protected ModelAndView getEditView(Module model){
        ModelAndView mav = super.getEditView(model);
        mav.addObject("ModuleTypes", ModuleType.values());
        mav.addObject("EventTypes", EventType.values());
        try {
            String fileContents = FileUtil.getFileContents("font-awesome-icon-names.txt");
            String[] iconNames = fileContents.split("\n");
            mav.addObject("FontAwesomeIconNames", iconNames);
        } catch (IOException ex) {
            LOG.warn("Unable to get list of font-aweseome icon names");
        }
        return mav;
    }
    
    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") Module model, HttpServletRequest request, BindingResult result){
        if (result.hasErrors()){
            return super.getEditView(model);
        }
        if (model.getId() == null){
            Module existingPageEntry = moduleDAO.findByTitle(model.getTitle());
            if (existingPageEntry != null){
                ModelAndView editView = getEditView(model);
                result.rejectValue("title", "ModuleWithTitleAlreadyExists", new Object[]{model.getTitle()}, "ModuleWithTitleAlreadyExists");
                return editView;
            }
        }
        
        ModelAndView mav = super.postEditView(model, request, result);
        reloadModules(request);
        return mav;
    }
    
    @Override
    public ModelAndView postDelete(HttpServletRequest request, @PathVariable("id") Long id){
        ModelAndView mav = super.postDelete(request, id);
        reloadModules(request);
        return mav;
    }
    
    @Override
    public void updateSortOrder(HttpServletRequest request, @ModelAttribute("Model") Module model, @RequestBody List<Long> orderedIds){
        super.updateSortOrder(request, model, orderedIds);
        reloadModules(request);
    }

    private void reloadModules(HttpServletRequest request) {
        moduleUtil.reloadModules(request);
    }
}
