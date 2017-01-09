/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.general;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.constants.ModuleType;
import de.appsolve.padelcampus.data.NestableItem;
import de.appsolve.padelcampus.db.dao.EventGroupDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.dao.ModuleDAOI;
import de.appsolve.padelcampus.db.dao.PageEntryDAOI;
import de.appsolve.padelcampus.db.model.EventGroup;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.PageEntry;
import de.appsolve.padelcampus.spring.EventGroupPropertyEditor;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import de.appsolve.padelcampus.utils.FileUtil;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import de.appsolve.padelcampus.utils.ModuleUtil;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/general/modules")
public class AdminGeneralModulesController extends AdminBaseController<Module> {
    
    private final static Logger LOG = Logger.getLogger(AdminGeneralModulesController.class);
    
    private final RequestMappingHandlerMapping handlerMapping;
    
    @Autowired
    public AdminGeneralModulesController(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Autowired
    ModuleDAOI moduleDAO;
    
    @Autowired
    PageEntryDAOI pageEntryDAO;
    
    @Autowired
    ModuleUtil moduleUtil;
    
    @Autowired
    EventGroupDAOI eventGroupDAO;
    
    @Autowired
    EventGroupPropertyEditor eventGroupPropertyEditor;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
        binder.registerCustomEditor(EventGroup.class, eventGroupPropertyEditor);
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
        ModelAndView editView = super.getEditView(model);
        editView.addObject("ModuleTypes", ModuleType.values());
        editView.addObject("EventGroups", eventGroupDAO.findAll());
        Boolean isRootModule = model.getId() == null;
        if (!isRootModule){
            isRootModule = moduleDAO.findAllRootModules().contains(model);
        }
        editView.addObject("isRootModule", isRootModule);
        try {
            String fileContents = FileUtil.getFileContents("font-awesome-icon-names.txt");
            String[] iconNames = fileContents.split("\n");
            editView.addObject("FontAwesomeIconNames", iconNames);
        } catch (IOException ex) {
            LOG.warn("Unable to get list of font-aweseome icon names", ex);
        }
        return editView;
    }
    
    @Override
    public ModelAndView postEditView(@ModelAttribute("Model") Module model, HttpServletRequest request, BindingResult result){
        checkTitleRequirements(model, result);
        if (result.hasErrors()){
            return getEditView(model);
        }
        keepSubModules(model);
        checkPosition(model);
        rewriteLinks(model);
        
        ModelAndView mav = super.postEditView(model, request, result);
        moduleUtil.reloadModules(request);
        return mav;
    }
    
    @Override
    public ModelAndView postDelete(HttpServletRequest request, @PathVariable("id") Long id){
        Module module = moduleDAO.findById(id);
        Module parent = moduleDAO.findParent(module);
        if (parent != null){
            //load again from DB because findParent() does not load all submodules
            parent = moduleDAO.findById(parent.getId());
            //causes IllegalArgumentException: Removing a detached instance Module
            //parent.getSubModules().remove(module);
            //same goes for: removeIf predicate and removing via iterator

            Set<Module> subModules = new TreeSet<>(parent.getSubModules());
            subModules.remove(module);
            parent.setSubModules(subModules);
            moduleDAO.saveOrUpdate(parent);
        }
        ModelAndView mav = super.postDelete(request, id);
        moduleUtil.reloadModules(request);
        return mav;
    }
    
    @RequestMapping(method=POST, value="/updateposition")
    @ResponseBody
    public List<Module> updatePosition(HttpServletRequest request, @RequestBody List<NestableItem> nestableItems){
        for (Module module: moduleDAO.findAll()){
            module.setSubModules(null);
            moduleDAO.saveOrUpdate(module);
        }
        
        Long position = 0L;
        List<Module> modules = new ArrayList<>();
        for (NestableItem nestableItem: nestableItems){
            modules.add(updateNestableItemPosition(nestableItem, position));
            position++;
        }
        moduleUtil.reloadModules(request);
        return modules;
    }
    
    private Module updateNestableItemPosition(NestableItem nestableItem, Long position){
        Module module = moduleDAO.findById(nestableItem.getId());
        module.setPosition(position);
        if (nestableItem.getChildren()==null){
            module.setSubModules(null);
        } else {
            Set<Module> subModules = new TreeSet<>();
            for (NestableItem subItem: nestableItem.getChildren()){
                subModules.add(updateNestableItemPosition(subItem, position++));
            }
            module.setSubModules(subModules);
        }
        module = moduleDAO.saveOrUpdate(module);
        return module;
    }
    
    private void checkTitleRequirements(Module module, BindingResult result) {
        if (result.hasErrors()){
            return;
        }
        
        //make sure title does not conflict with existing request mappings
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMapping.getHandlerMethods().entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            List<String> matchingPatterns = requestMappingInfo.getPatternsCondition().getMatchingPatterns("/"+module.getUrlTitle());
            for (String pattern: matchingPatterns){
                if (!pattern.equals("/{moduleId}")){
                    result.rejectValue("title", "ModuleWithUrlTitleAlreadyExists", new Object[]{module.getUrlTitle()}, "ModuleWithUrlTitleAlreadyExists");
                    return;
                }
            }
        }
        
        Module existingModule = moduleDAO.findByUrlTitle(module.getUrlTitle());
        if (existingModule != null && !existingModule.equals(module)){
            result.rejectValue("title", "ModuleWithUrlTitleAlreadyExists", new Object[]{module.getUrlTitle()}, "ModuleWithUrlTitleAlreadyExists");
        }
    }

    private void keepSubModules(Module model) {
        if (model.getId() != null){
            Module existing = moduleDAO.findById(model.getId());
            model.setSubModules(existing.getSubModules());
        }
    }

    @SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
    private void checkPosition(Module module) {
        if (module.getPosition() == null){
            Long position = 0L;
            List<Module> modules = moduleDAO.findAll();
            for (Module existing: modules){
                if (existing.getPosition() != null){
                    position = Math.max(position, existing.getPosition());
                }
            }
            position++;
            module.setPosition(position);
        }
    }

    private void rewriteLinks(Module model) {
        if (model.getId()!=null){
            Module existingModule = moduleDAO.findById(model.getId());
            if (!model.getUrlTitle().equals(existingModule.getUrlTitle())){
                String oldHref = String.format("href=\"(%s|/page%s)\"", existingModule.getUrl(), existingModule.getUrl());
                String newHref = String.format("href=\"%s\"", model.getUrl());
                Pattern p = Pattern.compile(oldHref, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                for (PageEntry pageEntry: pageEntryDAO.findAll()){
                    String message = pageEntry.getMessage();
                    if (!StringUtils.isEmpty(message)){
                        Matcher m = p.matcher(message);
                        if (m.find()){
                            LOG.info(String.format("replacing links %s by %s in page entry %s", oldHref, newHref, pageEntry.getId()));
                            pageEntry.setMessage(m.replaceAll(newHref));
                            pageEntryDAO.saveOrUpdate(pageEntry);
                        }
                    }
                    
                }
            }
        }
    }
}
