package com.sd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author D
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

  private String username;

  private String password;

  private String firstName;

  private String lastName;

  private String email;

}