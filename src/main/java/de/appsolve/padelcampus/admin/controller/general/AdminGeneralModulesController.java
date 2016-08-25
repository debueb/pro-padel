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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    public ModelAndView showIndex(HttpServletRequest request, Pageable pageable, @RequestParam(required = false, name = "search") String search){
        Page<Module> all = new PageImpl(moduleDAO.findAllRootModules());
        return getIndexView(all);
    }
    
    @Override
    protected ModelAndView getEditView(Module model){
        ModelAndView mav = super.getEditView(model);
        addEditObjects(mav);
        return mav;
    }
    
    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") Module model, HttpServletRequest request, BindingResult result){
        checkTitleRequirements(model, result);
        if (result.hasErrors()){
            return super.getEditView(model);
        }
        keepSubModules(model);
        
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
    
    @RequestMapping("/edit/{id}/submodules")
    public ModelAndView showSubmodules(@PathVariable("id") Long id){
        Module module = moduleDAO.findById(id);
        return getSubmoduleView(module);
    }
    
    @RequestMapping(value={"/edit/{id}/submodules/add"}, method=GET)
    public ModelAndView showSubModuleAddView(@PathVariable("id") Long id){
        return getSubmoduleEditView(id, createNewInstance());
    }
    
    @RequestMapping(value="/edit/{id}/submodules/edit/{modelId}", method=GET)
    public ModelAndView showSubmoduleEditView(@PathVariable("id") Long id, @PathVariable("modelId") Long modelId){
        return getSubmoduleEditView(id, findById(modelId));
    }
    
    @RequestMapping(value={"/edit/{id}/submodules/add", "/edit/{id}/submodules/edit/{modelId}"}, method=POST)
    public ModelAndView postSubmoduleEditView(@PathVariable("id") Long parentModuleId, @ModelAttribute("Model") Module model, HttpServletRequest request, BindingResult result){
        validator.validate(model, result);
        checkTitleRequirements(model, result);
        if (result.hasErrors()){
            return getEditView(model);
        }
        model.setShowInMenu(Boolean.TRUE);
        model.setShowInFooter(Boolean.FALSE);
        model.setShowOnHomepage(Boolean.FALSE);
        keepSubModules(model);
        moduleDAO.saveOrUpdate(model);
        Module parent = moduleDAO.findById(parentModuleId);
        Set<Module> subModules = parent.getSubModules();
        if (subModules == null){
            subModules = new HashSet<>();
        }
        subModules.add(model);
        parent.setSubModules(subModules);
        moduleDAO.saveOrUpdate(parent);
        reloadModules(request);
        return redirectToIndex(request);
    }
    
    @RequestMapping(value="/edit/{moduleId}/submodules/updatesortorder", method=POST)
    @ResponseStatus(OK)
    public void updateSubmoduleSortOrder(HttpServletRequest request, @ModelAttribute("Model") Module model, @RequestBody List<Long> orderedIds){
        super.updateSortOrder(request, model, orderedIds);
        reloadModules(request);
    }
    
    protected ModelAndView getSubmoduleView(Module module){
        ModelAndView mav = new ModelAndView(getModuleName()+"/submodules/index");
        mav.addObject("Parent", module);
        mav.addObject("Models", module.getSubModules());
        mav.addObject("moduleName", getModuleName());
        return mav;
    }
    
    protected ModelAndView getSubmoduleEditView(Long parentModuleId, Module model){
        ModelAndView mav =  new ModelAndView("/"+getModuleName()+"/submodules/edit");
        mav.addObject("Parent", moduleDAO.findById(parentModuleId));
        mav.addObject("Model", model);
        mav.addObject("moduleName", getModuleName());
        addEditObjects(mav);
        return mav;
    }
    
    private void reloadModules(HttpServletRequest request) {
        moduleUtil.reloadModules(request);
    }

    private void addEditObjects(ModelAndView mav) {
        mav.addObject("ModuleTypes", ModuleType.values());
        mav.addObject("EventTypes", EventType.values());
        try {
            String fileContents = FileUtil.getFileContents("font-awesome-icon-names.txt");
            String[] iconNames = fileContents.split("\n");
            mav.addObject("FontAwesomeIconNames", iconNames);
        } catch (IOException ex) {
            LOG.warn("Unable to get list of font-aweseome icon names");
        }
    }

    private void checkTitleRequirements(Module module, BindingResult result) {
        if (result.hasErrors()){
            return;
        }
        Module existingModule = moduleDAO.findByTitle(module.getTitle());
        if (existingModule != null && !existingModule.equals(module)){
            result.rejectValue("title", "ModuleWithTitleAlreadyExists", new Object[]{module.getTitle()}, "ModuleWithTitleAlreadyExists");
        }
    }

    private void keepSubModules(Module model) {
        if (model.getId() != null){
            Module existing = moduleDAO.findById(model.getId());
            model.setSubModules(existing.getSubModules());
        }
    }
}
