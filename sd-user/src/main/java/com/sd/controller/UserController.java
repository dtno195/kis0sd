package com.sd.controller;

import com.sd.dto.UserVO;
import com.sd.service.UserService;
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
@RequestMapping("/user")
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping("/")
  public String users(){
    return "Get all user working";
  }

  @PostMapping("/create")
  public ResponseEntity createUser(@RequestBody UserVO userVO) {
    userService.createUser(userVO);
    return ResponseEntity.status(HttpStatus.CREATED).body(userVO);
  }

  @GetMapping("/users")
  public ResponseEntity<List<UserVO>> getAllUser() {
    List<UserVO> userVOS = new ArrayList<>();
    userVOS.add(new UserVO("Ducnt1", "123", "Ducnt", "Dustin", "ducnt@gmail.com"));
    userVOS.add(new UserVO("Ducnt2", "456", "Ducnt2", "Dustin2", "ducnt2@gmail.com"));
    return ResponseEntity.ok(userVOS);
  }
}
