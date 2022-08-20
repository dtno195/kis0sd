package com.sd.service.impl;

import com.sd.common.enums.Authority;
import com.sd.common.enums.Language;
import com.sd.common.exception.BusinessException;
import com.sd.common.utils.Messages;
import com.sd.dto.UserVO;
import com.sd.dto.user.UserRequest;
import com.sd.dto.user.UserResponse;
import com.sd.entity.User;
import com.sd.repository.UserRepository;
import com.sd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

}
