/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.matchers;

import org.junit.Assert;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

/**
 *
 * @author dominik
 */
public class ModelAttributeToStringEquals implements ResultMatcher{
    
    private final String attribute;
    private final String value;
    
    public ModelAttributeToStringEquals(String attribute, String value){
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public void match(MvcResult result) throws Exception {
        Object object = result.getModelAndView().getModel().get(attribute);
        Assert.assertNotNull(object);
        if (!object.toString().equals(value)){
            System.out.println("exp: "+value);
            System.out.println("got: "+object.toString());
        }
        Assert.assertTrue(object.toString().equals(value));
    }
    
}
