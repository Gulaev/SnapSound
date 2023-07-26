package com.gulaev.SnapSound.web;


import com.gulaev.SnapSound.payload.request.LoginRequest;
import com.gulaev.SnapSound.payload.request.SignupRequest;
import com.gulaev.SnapSound.payload.response.JWTTokenSuccessResponse;
import com.gulaev.SnapSound.payload.response.MessageResponse;
import com.gulaev.SnapSound.security.JWTTokenProvider;
import com.gulaev.SnapSound.security.SecurityConstant;
import com.gulaev.SnapSound.service.ResponseErrorValidation;
import com.gulaev.SnapSound.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

  private AuthenticationManager authenticationManager;
  private JWTTokenProvider jwtTokenProvider;
  private ResponseErrorValidation responseErrorValidation;
  private UserService userService;

  @Autowired
  public AuthController(AuthenticationManager authenticationManager,
      JWTTokenProvider jwtTokenProvider, ResponseErrorValidation responseErrorValidation,
      UserService userService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.responseErrorValidation = responseErrorValidation;
    this.userService = userService;
  }

  @PostMapping("/signin")
  public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
    ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
    if (!ObjectUtils.isEmpty(errors)) return errors;

    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = SecurityConstant.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt ));
  }

  @PostMapping("/signup")
  public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
    ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
    if (!ObjectUtils.isEmpty(errors)) return errors;
      userService.createUser(signupRequest);
      return ResponseEntity.ok(new MessageResponse("User register successfully"));
  }
}
