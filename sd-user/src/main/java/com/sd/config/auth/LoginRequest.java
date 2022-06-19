package com.sd.config.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author D
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
  private String username;
  private String password;
}
