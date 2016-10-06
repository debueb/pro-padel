/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.validation.validators;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
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
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(phoneNo, "DE");
            boolean isValid = phoneUtil.isValidNumber(phoneNumber);
            if (!isValid){
                phoneNumber = phoneUtil.parse(phoneNo, "ZZ");
                isValid = phoneUtil.isValidNumber(phoneNumber);
            }
            return isValid;
        } catch (NumberParseException ex) {
            //emtpy
        }
        return false;
    }
}

