/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller;

import de.appsolve.padelcampus.db.model.BaseEntity;
import de.appsolve.padelcampus.utils.Msg;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 * @param <T>
 */
public abstract class BaseEntityController<T extends BaseEntity> extends BaseController implements BaseEntityControllerI {
    
    private static final Logger log = Logger.getLogger(BaseEntityController.class);
    
    @Autowired
    protected Msg msg;
    
    @RequestMapping(value = "/{id}/delete")
    public ModelAndView getDelete(@PathVariable("id") Long id){
        T model = (T)getDAO().findById(id);
        return getDeleteView(model);
    }
    
    @RequestMapping(value = "/{id}/delete", method = POST)
    public ModelAndView postDelete(HttpServletRequest request, @PathVariable("id") Long id){
        try {
            getDAO().deleteById(id);
        } catch (DataIntegrityViolationException e){
            T model = (T)getDAO().findById(id);
            log.warn("Attempt to delete "+model+" failed due to "+e);
            ModelAndView deleteView = getDeleteView(model);
            deleteView.addObject("error", msg.get("CannotDeleteDueToRefrence", new Object[]{model.toString()}));
            return deleteView;
        }
        return redirectToIndex(request);
    }
    
    protected ModelAndView redirectToIndex(HttpServletRequest request) {
        return new ModelAndView("redirect:/"+getModuleName());
    }
    
    
    protected ModelAndView getDeleteView(T model) {
        return new ModelAndView("/include/delete", "Model", model);
    }
}
