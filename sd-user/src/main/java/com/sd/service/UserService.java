package com.sd.service;

import com.sd.common.exception.BusinessException;
import com.sd.dto.user.UserRequest;
import com.sd.dto.user.UserResponse;

import java.time.OffsetDateTime;

/**
 * @author D
 */
public interface UserService {

  /**
   * Create new user.
   *
   * @param userRequest the new user
   * @return the user response
   * @throws BusinessException the business exception
   */
  UserResponse createUser(UserRequest userRequest);

  /**
   * Gets by id.
   *
   * @param id the id
   * @return the user response
   * @throws BusinessException the business exception
   */
  UserResponse getById(long id) throws BusinessException;

  /**
   * Update info user.
   *
   * @param updateUser the updated user
   * @return the user response
   * @throws BusinessException the business exception
   */
  UserResponse update(UserRequest updateUser) throws BusinessException;

  /**
   * Delete user by id.
   *
   * @param id the id
   * @throws BusinessException the business exception
   */
  void delete(long id) throws BusinessException;

  /**
   * Update last login time.
   *
   * @param id        the user id
   * @param loginTime the login time
   * @throws BusinessException the business exception
   */
//  void updateLastLoginTime(long id, OffsetDateTime loginTime) throws BusinessException;
}
