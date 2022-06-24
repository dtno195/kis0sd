//package com.sd.config.auth;
//
//import com.sd.entity.UserEntity;
//import com.sd.repository.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
///**
// * @author D
// */
//@Slf4j
//@Service
//public class SdUserDetailsService implements UserDetailsService {
//
//  private final UserRepository userRepository;
//
//  public SdUserDetailsService(UserRepository userRepository) {
//    this.userRepository = userRepository;
//  }
//
//  @Override
//  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    log.debug("load user by user name :" + username);
//    UserEntity userVO = userRepository.findByUsername(username);
//    if (null == userVO) {
//      throw new UsernameNotFoundException("Not found user: " + username);
//    }
//    Collection<GrantedAuthority> gs = new ArrayList<>();
//    // get role
//    // for role add GrantedAuthority
//    User userDetails = new User
//            (userVO.getUsername(),
//                    userVO.getEncryptedPassword(),
//                    true,
//                    true,
//                    true,
//                    true,
//                    gs);
//    return userDetails;
//  }
//}
