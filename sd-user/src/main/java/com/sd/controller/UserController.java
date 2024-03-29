package com.sd.controller;

import com.sd.common.exception.BusinessException;
import com.sd.dto.user.UserRequest;
import com.sd.dto.user.UserResponse;
import com.sd.dto.user.UserSearchRequest;
import com.sd.service.UserService;
import com.sd.util.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

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


  /**
   * Gets all user paging.
   *
   * @param pagingRequest the paging request
   * @return the all user paging
   */
  @GetMapping
  public ResponseEntity<Page<UserResponse>> getAllUserPaging(UserSearchRequest pagingRequest) {
    Pageable paging = AppUtils.getPaging(pagingRequest);
    return ResponseEntity.ok().body(this.userService.getAllPaging(pagingRequest.getKeySearch(), pagingRequest.getAuthority(), paging));
  }

  @PostMapping
  public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.createUser(userRequest));
  }

  /**
   * Gets one user.
   *
   * @param id the id
   * @return the one user
   * @throws BusinessException the business exception
   */
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getOneUser(@PathVariable(name = "id") Integer id) throws BusinessException {
    return ResponseEntity.ok().body(this.userService.getById(id));
  }

  /**
   * Update user by id.
   *
   * @param userId      the user id
   * @param userRequest the user request
   * @return the response
   * @throws BusinessException the business exception
   */
  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> update(@PathVariable(name = "id") Long userId,
                                             @Valid @RequestBody UserRequest userRequest) throws BusinessException {
    userRequest.setId(userId);
    return ResponseEntity.ok().body(this.userService.update(userRequest));
  }

  /**
   * Delete user by id.
   *
   * @param userId the user id
   * @return the response
   * @throws BusinessException the business exception
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> delete(@PathVariable(name = "id") Long userId) throws BusinessException {
    this.userService.delete(userId);
    return ResponseEntity.ok().body(true);
  }
}
