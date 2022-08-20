package com.sd.controller;

import com.sd.dto.UserVO;
import com.sd.dto.user.UserRequest;
import com.sd.dto.user.UserResponse;
import com.sd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author D
 */
@RestController
@RequestMapping("/webapi/v1/user")
@RequiredArgsConstructor
public class UserController {
  /**
   * The User service.
   */
  protected final UserService userService;

  @GetMapping("/")
  public String users() {
    return "Get all user working";
  }

  @PostMapping
  public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.createUser(userRequest));
  }

}
