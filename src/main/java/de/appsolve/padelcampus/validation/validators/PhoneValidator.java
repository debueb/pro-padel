/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.validation.validators;

import de.appsolve.padelcampus.validation.constraints.Phone;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author dominik
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
 
    @Override
    public void initialize(Phone paramA) {
    }
 
    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext ctx) {
        if(phoneNo == null || phoneNo.equals("")){
            return true; //we return true so we stay compatible with @NotEmpty
        }
        return phoneNo.matches("^(0|\\+[0-9]{1,2}|00[0-9]{1,2}|\\([0-9]{3,6}\\))(\\s)*(\\(0\\))*(\\s)*([0-9]{1,}|\\s|-|\\/)*$");
    }
 
}

