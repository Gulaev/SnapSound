package com.gulaev.SnapSound.annotations;

import com.gulaev.SnapSound.validations.PasswordMatchesValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {

  String message() default "Passwords do not match";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}