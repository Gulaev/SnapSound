package com.gulaev.SnapSound.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDTO {

  private Long id;
  @NotEmpty
  private String username;
  @NotEmpty
  private String firstName;
  @NotEmpty
  private String lastName;
  private String bio;

}
