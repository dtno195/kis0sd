package com.sd.dto.user.auth;

import com.sd.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dustin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {

  /**
   * The Refresh token.
   */
  private String refreshToken;

  /**
   * The JWT token.
   */
  private String jwtToken;

  /**
   * The User.
   */
  private UserResponse user;
}
