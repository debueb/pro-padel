/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.validation.constraints;

import de.appsolve.padelcampus.validation.validators.SelfValidatingValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author dominik
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SelfValidatingValidator.class)
@Documented
public @interface SelfValidating {

    String message() default "{SelfValidatingInvalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
