package com.sd.service.refresh_token;

import com.sd.common.exception.BusinessException;
import com.sd.config.auth.RefreshTokenResponse;
import com.sd.entity.RefreshToken;
import lombok.NonNull;

import java.util.List;

/**
 * @author Dustin
 */
public interface RefreshTokenService {
  /**
   * Create refresh token.
   *
   * @param userId the user id
   * @return the refresh token
   * @throws BusinessException the business exception
   */
  RefreshToken create(int userId) throws BusinessException;

  /**
   * Delete by ids.
   *
   * @param ids the ids
   * @throws BusinessException the business exception
   */
  void deleteByIds(@NonNull List<Integer> ids) throws BusinessException;

  /**
   * Handle refresh token.
   *
   * @param refreshTokenUsing the refresh token using
   * @return the refresh token response
   */
  RefreshTokenResponse refreshToken(@NonNull String refreshTokenUsing);
}
