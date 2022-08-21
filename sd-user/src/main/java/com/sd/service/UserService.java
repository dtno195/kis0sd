package com.sd.service;

import com.sd.common.enums.Authority;
import com.sd.common.exception.BusinessException;
import com.sd.dto.user.UserRequest;
import com.sd.dto.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
   * Gets all paging.
   *
   * @param pageRequest the page request
   * @return the user paging
   */
  Page<UserResponse> getAllPaging(String keySearch, Authority authority, Pageable pageRequest);

  /**
   * Update last login time.
   *
   * @param id        the user id
   * @param loginTime the login time
   * @throws BusinessException the business exception
   */
//  void updateLastLoginTime(long id, OffsetDateTime loginTime) throws BusinessException;
}
