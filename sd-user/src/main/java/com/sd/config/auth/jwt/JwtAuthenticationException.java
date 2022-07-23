package com.sd.config.auth.jwt;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Dustin
 */
public class JwtAuthenticationException extends AuthenticationException {
  /**
   * Constructs an {@code AuthenticationException} with the specified message and no root cause.
   *
   * @param msg the detail message
   */
  public JwtAuthenticationException(String msg) {
    super(msg);
  }
}
