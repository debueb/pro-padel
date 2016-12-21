/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.validation.constraints;

import de.appsolve.padelcampus.validation.validators.SelfValidatingValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author dominik
 */
@Target( { ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SelfValidatingValidator.class)
@Documented
public @interface SelfValidating {

    String message() default "{SelfValidatingInvalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}