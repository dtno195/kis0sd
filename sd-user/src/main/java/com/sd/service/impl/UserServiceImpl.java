package com.sd.service.impl;

import com.sd.common.enums.Authority;
import com.sd.common.enums.Language;
import com.sd.common.exception.BusinessException;
import com.sd.common.exception.BusinessMessage;
import com.sd.common.exception.ResponseCode;
import com.sd.common.utils.Messages;
import com.sd.dto.user.UserRequest;
import com.sd.dto.user.UserResponse;
import com.sd.entity.User;
import com.sd.repository.UserRepository;
import com.sd.service.UserService;
import com.sd.util.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @author D
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

  /**
   * The User repository.
   */
  protected final UserRepository userRepository;

  /**
   * The Password encoder.
   */
  protected final PasswordEncoder passwordEncoder;

  /**
   * Create new user.
   *
   * @param userRequest the new user
   * @return the user response
   * @throws BusinessException the business exception
   */
  @Override
  public UserResponse createUser(UserRequest userRequest) {

    if (!userRequest.getPassword().equals(userRequest.getPasswordConfirmed())) {
      throw new BusinessException(Messages.PASSWORD_NOT_MATCH);
    }

    Optional<User> user = this.userRepository.findByUsernameAndIsDeletedFalse(userRequest.getUsername());
    if (user.isPresent()) {
      throw new BusinessException(Messages.USER_EXISTED);
    }
    User newUserEntity = UserRequest.toEntity(userRequest);
    newUserEntity.setLanguage(Objects.nonNull(userRequest.getLanguage()) ? userRequest.getLanguage() : Language.Vietnam);
    newUserEntity.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
    newUserEntity.setAuthority(Objects.nonNull(userRequest.getAuthority()) ? userRequest.getAuthority() : Authority.USER);

    return UserResponse.fromEntity(this.userRepository.saveAndFlush(newUserEntity));
  }

  @Transactional(readOnly = true)
  @Override
  public UserResponse getById(long id) throws BusinessException {
    return this.userRepository
            .findById(id)
            .map(UserResponse::fromEntity)
            .orElseThrow(() -> new BusinessException(BusinessMessage.NOT_FOUND, ResponseCode.CODE_BUSINESS));
  }


  /**
   * Update info user.
   *
   * @param updateUser the updated user
   * @return the user response
   * @throws BusinessException the business exception
   */
  @Override
  public UserResponse update(UserRequest updateUser) throws BusinessException {
    User currentUser = this.userRepository.findById(updateUser.getId())
                                          .orElseThrow(() -> new BusinessException(BusinessMessage.NOT_FOUND));
    if(StringUtils.hasText(updateUser.getPassword())) {
      if(!updateUser.getPassword().equals(updateUser.getPasswordConfirmed())){
        throw new BusinessException(Messages.PASSWORD_NOT_MATCH);
      }
      currentUser.setPassword(this.passwordEncoder.encode(updateUser.getPassword()));
    }
    // Không cho cập nhật username
    currentUser.setAuthority(Objects.nonNull(updateUser.getAuthority()) ? updateUser.getAuthority() : currentUser.getAuthority());
    currentUser.setFirstName(updateUser.getFirstName());
    currentUser.setLastName(updateUser.getLastName());
    currentUser.setPhone(updateUser.getPhone());
    currentUser.setMail(updateUser.getMail());
    return UserResponse.fromEntity(this.userRepository.save(currentUser));
  }

  /**
   * Delete user.
   *
   * @param id the id
   * @throws BusinessException the business exception
   */
  @Override
  public void delete(long id) throws BusinessException {
    this.userRepository.deleteById(id);
  }

  @Override
  public Page<UserResponse> getAllPaging(String keySearch, Authority authority, Pageable pageRequest) {
    return this.userRepository.findAll(keySearch, authority, pageRequest).map(UserResponse::fromEntity);
  }

  /**
   * Gets available user by username.
   *
   * @param username the username
   * @return the user response
   * @throws BusinessException the business exception
   */
  @Transactional(readOnly = true)
  @Override
  public UserResponse getByUsername(String username) throws BusinessException {
    return this.userRepository.findByUsernameAndIsDeletedFalse(username)
            .map(UserResponse::fromEntity)
            .orElseThrow(() -> new BusinessException(BusinessMessage.NOT_FOUND, ResponseCode.CODE_BUSINESS));
  }


  /**
   * Update last login time.
   *
   * @param id        the user id
   * @param loginTime the login time
   * @throws BusinessException the business exception
   */
//  @Override
//  public void updateLastLoginTime(long id, OffsetDateTime loginTime) throws BusinessException {
//    this.userRepository.updateLastLoginTime(id, loginTime);
//  }

}
