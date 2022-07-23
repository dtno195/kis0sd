package com.sd.config.auth.user;

import com.sd.common.exception.BusinessMessage;
import com.sd.entity.User;
import com.sd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Dustin
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  /**
   * The User repository.
   */
  private final UserRepository userRepository;


  /**
   * Load user by username.
   *
   * @param username the username
   * @return the user details
   * @throws UsernameNotFoundException the username not found exception
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.userRepository.findByUsernameAndIsDeletedFalse(username)
                                   .orElseThrow(() -> new UsernameNotFoundException(BusinessMessage.INCORRECT_USERNAME_PASSWORD));
    return new UserDetailsImpl(user.getId(), username, user.getPassword(), user.getAuthority());
  }
}
