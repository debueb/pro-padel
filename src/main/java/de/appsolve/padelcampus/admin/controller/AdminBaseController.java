/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller;

import de.appsolve.padelcampus.controller.BaseEntityControllerI;
import de.appsolve.padelcampus.controller.BaseEntityController;
import de.appsolve.padelcampus.db.model.BaseEntityI;
import java.lang.reflect.ParameterizedType;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
/**
 *
 * @author dominik
 * @param <T>
 */
public abstract class AdminBaseController<T extends BaseEntityI> extends BaseEntityController<T>{
    
    protected static final Logger LOG = Logger.getLogger(AdminBaseController.class);
    
    @Autowired
    protected Validator validator;
    
    @RequestMapping()
    public ModelAndView showIndex(HttpServletRequest request, Pageable pageable, @RequestParam(required = false, name = "search") String search){
        Page<T> all;
        if (!StringUtils.isEmpty(search)){
            all = findAllByFuzzySearch(search);
        } else {
            all = findAll(pageable);
        }
        return getIndexView(all);
    }
    
    @RequestMapping(value={"add"}, method=GET)
    public ModelAndView showAddView(){
        return getEditView(createNewInstance());
    }
    
    @RequestMapping(value="edit/{modelId}", method=GET)
    public ModelAndView showEditView(HttpServletRequest request, @PathVariable("modelId") Long modelId){
        return getEditView(findById(modelId));
    }
    
    @RequestMapping(value={"add", "edit/{modelId}"}, method=POST)
    @SuppressWarnings("unchecked")
    public ModelAndView postEditView(@ModelAttribute("Model") T model, HttpServletRequest request, BindingResult result){
        validator.validate(model, result);
        if (result.hasErrors()){
            return getEditView(model);
        }
        getDAO().saveOrUpdate(model);
        return redirectToIndex(request);
    }
    
    protected ModelAndView getIndexView(Page<T> page){
        ModelAndView mav = new ModelAndView(getModuleName()+"/index");
        mav.addObject("Page", page);
        mav.addObject("Models", page.getContent());
        mav.addObject("moduleName", getModuleName());
        return mav;
    }
    
    protected ModelAndView getEditView(T model){
        ModelAndView mav =  new ModelAndView("/"+getModuleName()+"/edit");
        mav.addObject("Model", model);
        mav.addObject("moduleName", getModuleName());
        return mav;
    }
    
    @SuppressWarnings("unchecked")
    protected Page<T> findAll(Pageable pageable) {
        return getDAO().findAll(pageable);
    }
    
    @SuppressWarnings("unchecked")
    protected Page<T> findAllByFuzzySearch(String search) {
        return getDAO().findAllByFuzzySearch(search);
    }

    @SuppressWarnings("unchecked")
    protected T findById(Long modelId) {
        return (T) getDAO().findById(modelId);
    }
    
    @SuppressWarnings("unchecked")
    protected T createNewInstance(){
        try {
            Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e){
            LOG.error(e);
        }
        return null;
    }
}
