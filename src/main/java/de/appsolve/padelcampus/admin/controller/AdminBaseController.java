/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller;

import de.appsolve.padelcampus.controller.BaseEntityControllerI;
import de.appsolve.padelcampus.controller.BaseEntityController;
import de.appsolve.padelcampus.db.model.BaseEntity;
import de.appsolve.padelcampus.utils.Msg;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;
/**
 *
 * @author dominik
 * @param <T>
 */
public abstract class AdminBaseController<T extends BaseEntity> extends BaseEntityController<T> implements BaseEntityControllerI{
    
    private static final Logger log = Logger.getLogger(AdminBaseController.class);
    
    @Autowired
    protected Validator validator;
    
    @Autowired
    protected Msg msg;
    
    @RequestMapping()
    public ModelAndView showIndex(){
        ModelAndView mav = new ModelAndView("admin/"+getModuleName()+"/index");
        List all = getDAO().findAll();
        Collections.sort(all);
        mav.addObject("Models", all);
        return mav;
    }
    
    @RequestMapping(value={"add"}, method=GET)
    public ModelAndView showAddView(){
        return getEditView(createNewInstance());
    }
    
    @RequestMapping(value="edit/{modelId}", method=GET)
    public ModelAndView showEditView(@PathVariable("modelId") Long modelId){
        return getEditView((T)getDAO().findById(modelId));
    }
    
    @RequestMapping(value={"add", "edit/{modelId}"}, method=POST)
    public ModelAndView postEditView(@ModelAttribute("Model") T model, HttpServletRequest request, BindingResult result){
        validator.validate(model, result);
        if (result.hasErrors()){
            return getEditView(model);
        }
        getDAO().saveOrUpdate(model);
        return redirectToIndex();
    }
    
    protected ModelAndView getEditView(T model){
        return new ModelAndView("/"+getModuleName()+"/edit", "Model", model);
    }

    @SuppressWarnings("unchecked")
    private T createNewInstance(){
        try {
            Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e){
            log.error(e);
        }
        return null;
    }
}
