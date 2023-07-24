package com.gulaev.SnapSound.validations;

import com.gulaev.SnapSound.annotations.PasswordMatches;
import com.gulaev.SnapSound.payload.request.SignupRequest;
import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {


  @Override
  public void initialize(PasswordMatches constraintAnnotation) {
  }

  @Override 
  public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
    SignupRequest userSignupRequest = (SignupRequest) o;
    return userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword()); 
  }
}
