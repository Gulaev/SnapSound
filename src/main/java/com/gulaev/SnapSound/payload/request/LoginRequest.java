package com.gulaev.SnapSound.payload.request;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {

  @NotEmpty(message = "Username cannot by empty")
  private String username;

  @NotEmpty(message = "Password cannot by empty")
  private String password;
}
