package com.sd.controller;

import com.sd.dto.UserVO;
import com.sd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author D
 */
@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping("/message")
  public String test() {
    return "Hello, this is user service";
  }

  @GetMapping("/")
  public String users(){
    return "Get all user working";
  }

  @PostMapping("/create")
  public ResponseEntity createUser(@RequestBody UserVO userVO) {
    userService.createUser(userVO);

    return ResponseEntity.status(HttpStatus.CREATED).body(userVO);
  }
}
