package com.sd.controller.auth;

import com.sd.common.exception.BusinessException;
import com.sd.config.auth.RefreshTokenResponse;
import com.sd.config.auth.jwt.JwtProvider;
import com.sd.config.auth.user.UserDetailsImpl;
import com.sd.dto.user.UserResponse;
import com.sd.dto.user.auth.SignInRequest;
import com.sd.dto.user.auth.SignInResponse;
import com.sd.service.UserService;
import com.sd.service.refresh_token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * @author Dustin
 */
@RestController
@RequestMapping("/webapi/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  /**
   * The Authentication manager.
   */
  protected final AuthenticationManager authenticationManager;

  /**
   * The Jwt provider.
   */
  protected final JwtProvider jwtProvider;

  /**
   * The User service.
   */
  protected final UserService userService;

  /**
   * The Refresh token service.
   */
  protected final RefreshTokenService refreshTokenService;

  /**
   * Sign in.
   *
   * @param signInRequest the sign in request
   * @return the response
   */
  @PostMapping("/sign-in")
  public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) throws Exception {
    final OffsetDateTime signInTime = OffsetDateTime.now(ZoneOffset.UTC);

    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
            = new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword());

    Authentication authentication = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserResponse loginUser = this.userService.getByUsername(((UserDetailsImpl) authentication.getPrincipal()).getUsername());

    this.userService.updateLastLoginTime(loginUser.getId(), signInTime);
    loginUser.setLastLoginTime(signInTime);
    final String refreshToken = this.refreshTokenService.create(loginUser.getId()).getToken();

    final String jwtToken = this.jwtProvider.generateToken(loginUser.getUsername());
    System.out.println(jwtToken);
    return ResponseEntity.ok().body(new SignInResponse(refreshToken, jwtToken, loginUser));
  }

  /**
   * Handle refresh token.
   *
   * @param refreshToken the refresh token
   * @return the response
   * @throws BusinessException the business exception
   */
  @PostMapping("/refresh-token")
  public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody String refreshToken) throws BusinessException {
    return ResponseEntity.ok().body(this.refreshTokenService.refreshToken(refreshToken));
  }
}
