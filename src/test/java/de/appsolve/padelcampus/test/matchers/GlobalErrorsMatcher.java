/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.ModelResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
public class GlobalErrorsMatcher extends ModelResultMatchers {

    private GlobalErrorsMatcher() {
    }

    public static GlobalErrorsMatcher globalErrors() {
        return new GlobalErrorsMatcher();
    }

    public ResultMatcher hasGlobalError(final String modelName, final String attribute, final String expectedMessage) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) throws Exception {
                BindingResult bindingResult = getBindingResult(result.getModelAndView(), modelName);
                for (ObjectError oe: bindingResult.getGlobalErrors()){
                    if (attribute.equals(oe.getObjectName())){
                        assertEquals("Expected default message", expectedMessage, oe.getDefaultMessage());
                    }
                }
            }
        };
    }

    private BindingResult getBindingResult(ModelAndView mav, String name) {
        BindingResult result = (BindingResult) mav.getModel().get(BindingResult.MODEL_KEY_PREFIX + name);
        assertTrue("No BindingResult for attribute: " + name, result != null);
        assertTrue("No global errors for attribute: " + name, result.getGlobalErrorCount() > 0);
        return result;
    }
}