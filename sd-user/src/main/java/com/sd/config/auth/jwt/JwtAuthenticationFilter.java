package com.sd.config.auth.jwt;

import com.sd.config.auth.user.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Dustin
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  /**
   * The Authorization header.
   */
  private final String AUTHORIZATION_HEADER = "Authorization";

  /**
   * The Bearer.
   */
  private final String BEARER = "Bearer";

  /**
   * The Jwt provider.
   */
  @Autowired
  private JwtProvider jwtProvider;

  /**
   * The User details service.
   */
  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  /**
   * The Handler exception resolver.
   */
  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver exceptionResolver;

  /**
   * The Jwt authentication entry point.
   */
  @Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

  }
}
