/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Workaround implementation for Hibernate's {@code @Email} annotation.
 * <p>
 * To require top-level domain I compose {@code @Email} annotation with
 * {@code @Pattern} where latter just add more strict checking.
 *
 * @author Slava Semushin <slava.semushin@gmail.com>
 * @see <a href="http://stackoverflow.com/q/4459474">Question at StackOverflow</a>
 * @see <a href="https://hibernate.onjira.com/browse/HVAL-43">Issue in Hibernate's bugtracker</a>
 * @since 10.01.2012
 */
@org.hibernate.validator.constraints.Email
@Pattern(regexp = ".+@.+\\.[a-zA-Z]{2,}")
@ReportAsSingleViolation
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface EmailWithTld {
    String message() default "{Email}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
