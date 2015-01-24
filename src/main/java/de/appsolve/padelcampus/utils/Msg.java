/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class Msg {
    
    @Autowired
    ApplicationContext context;
    
    public String get(String code){
        return get(code, null);
    }
    
    public String get(String code, Object[] args){
        try {
            return context.getMessage(code, args, null);
        } catch (NoSuchMessageException e){
            return code;
        }
    }
}
