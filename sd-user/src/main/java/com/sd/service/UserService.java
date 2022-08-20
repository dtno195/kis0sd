package com.sd.service;

import com.sd.dto.user.UserRequest;
import com.sd.dto.user.UserResponse;

/**
 * @author D
 */
public interface UserService {
  UserResponse createUser(UserRequest userRequest);
}
