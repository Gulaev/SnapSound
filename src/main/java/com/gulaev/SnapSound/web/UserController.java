package com.gulaev.SnapSound.web;

import com.gulaev.SnapSound.dto.UserDTO;
import com.gulaev.SnapSound.entity.User;
import com.gulaev.SnapSound.facade.UserFacade;
import com.gulaev.SnapSound.service.ResponseErrorValidation;
import com.gulaev.SnapSound.service.UserService;
import java.security.Principal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {

  @Autowired
  private UserService userService;
  @Autowired
  private UserFacade userFacade;
  @Autowired
  private ResponseErrorValidation errorValidation;

  @GetMapping("/")
  public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
    User user = userService.getCurrentUser(principal);
    UserDTO userDTO = userFacade.userToUserDTO(user);

    return new ResponseEntity<>(userDTO, HttpStatus.OK);
  }

  @GetMapping("{userId}")
  public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId) {
    User user =userService.getUserById(Long.parseLong(userId));
    UserDTO userDTO = userFacade.userToUserDTO(user);
    return new ResponseEntity<>(userDTO, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO,
      BindingResult bindingResult, Principal principal) {
    ResponseEntity<Object> errors = errorValidation.mapValidationService(bindingResult);
    if (!ObjectUtils.isEmpty(errors)) return errors;
    User user = userService.userUpdate(userDTO,principal);
    UserDTO userUpdated = userFacade.userToUserDTO(user);
     return new ResponseEntity<>(userUpdated, HttpStatus.OK);
  }

}
