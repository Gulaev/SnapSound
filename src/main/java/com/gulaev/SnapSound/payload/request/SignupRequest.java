package com.gulaev.SnapSound.payload.request;

import com.gulaev.SnapSound.annotations.PasswordMatches;
import com.gulaev.SnapSound.annotations.ValidEmail;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class SignupRequest {

  @Email(message = "It should have email format")
  @NotBlank(message = "User email is required")
  @ValidEmail
  private String email;
  @NotEmpty(message = "Please enter your name")
  private String firstName;
  @NotEmpty(message = "Please enter your last name")
  private String lastName;
  @NotEmpty(message = "Please enter your username")
  private String username;
  @NotEmpty(message = "Password is required")
  @Size(min = 6)
  private String password;
  @NotEmpty(message = "Password is required")
  @Size(min = 6)
  private String confirmPassword;

}
