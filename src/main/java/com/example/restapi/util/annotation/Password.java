package com.example.restapi.util.annotation;

import com.example.restapi.util.annotation.impl.PasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface Password {
    String message() default "must be a well-formed password. 1 upper, 1 lower letter, more 8 characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}