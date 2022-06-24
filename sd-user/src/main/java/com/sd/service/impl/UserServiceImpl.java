package com.sd.service.impl;

import com.sd.dto.UserVO;
import com.sd.entity.UserEntity;
import com.sd.repository.UserRepository;
import com.sd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author D
 */
@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

//  @Autowired
//  BCryptPasswordEncoder encoder;

  @Override
  public UserVO createUser(UserVO userVO) {
//    ModelMapper modelMapper = new ModelMapper();
//    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//    UserEntity userEntity = modelMapper.map(userVO, UserEntity.class);
////    userEntity.setEncryptedPassword(encoder.encode(userVO.getPassword()));
//    userEntity.setUserId(UUID.randomUUID().toString());
//    userRepository.save(userEntity);
    return userVO;
  }

}
