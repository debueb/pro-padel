/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.validation.validators;

import de.appsolve.padelcampus.validation.constraints.SelfValidating;
import de.appsolve.padelcampus.validation.constraints.Validatable;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author dominik
 */
public class SelfValidatingValidator implements ConstraintValidator<SelfValidating, Validatable> {

    public void initialize(SelfValidating constraintAnnotation) {}

    public boolean isValid(Validatable value,
        ConstraintValidatorContext constraintValidatorContext) {

        return value.isValid();
    }
}