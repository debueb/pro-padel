/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class Msg {
    
    private static final Logger LOG = Logger.getLogger(Msg.class);
    
    @Autowired
    ApplicationContext context;
    
    public String get(String code){
        return get(code, null);
    }
    
    public String get(String code, Object[] args){
        try {
            return context.getMessage(code, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e){
            LOG.warn("Missing translation for key "+code, e);
            return code;
        }
    }
}
