package com.sd.config.auth;

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
public class RefreshTokenResponse {
  /**
   * The Refresh token.
   */
  private String refreshToken;

  /**
   * The JWT token.
   */
  private String jwtToken;
}
