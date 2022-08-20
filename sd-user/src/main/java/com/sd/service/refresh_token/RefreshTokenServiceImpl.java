package com.sd.service.refresh_token;

import com.sd.common.exception.BusinessException;
import com.sd.common.exception.BusinessMessage;
import com.sd.common.exception.ResponseCode;
import com.sd.config.auth.RefreshTokenResponse;
import com.sd.config.auth.jwt.JwtProvider;
import com.sd.entity.RefreshToken;
import com.sd.repository.RefreshTokenRepository;
import com.sd.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

/**
 * @author Dustin
 */

@RequiredArgsConstructor
@Transactional
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

  /**
   * The Seconds block use refresh token.
   */
  private final int SECONDS_BLOCK_USE_REFRESH_TOKEN = 60;

  /**
   * The Expiration days.
   */
  @Value("${security.refresh-token.expiration}")
  private long EXPIRATION_DAYS;

  /**
   * The Jwt provider.
   */
  protected final JwtProvider jwtProvider;

  /**
   * The Refresh token repo.
   */
  protected final RefreshTokenRepository refreshTokenRepo;

  /**
   * The User repository.
   */
  protected final UserRepository userRepository;

  @Override
  public RefreshToken create(int userId) throws BusinessException {

    this.refreshTokenRepo.deactivateAllByUserId(userId);
    final LocalDate expiryDate = LocalDate.now(ZoneOffset.UTC).plusDays(this.EXPIRATION_DAYS);
    RefreshToken newRefreshToken = RefreshToken.builder()
            .token(UUID.randomUUID().toString())
            .userId(userId)
            .isActive(true)
            .expiryDate(expiryDate)
            .build();
    return this.refreshTokenRepo.save(newRefreshToken);
  }

  @Override
  public void deleteByIds(@NonNull List<Integer> ids) throws BusinessException {
    boolean hasInvalidId = ids.stream().anyMatch(id -> (id == null || id < 0));

    if (ids.isEmpty() || hasInvalidId) {
      throw new BusinessException(BusinessMessage.INVALID_VALUE, ResponseCode.CODE_VALIDATION);
    }

    this.refreshTokenRepo.deleteAllById(ids);
  }

  @Override
  public RefreshTokenResponse refreshToken(@NonNull String refreshTokenUsing) {
    RefreshToken currentRefreshToken = this.refreshTokenRepo
            .findByTokenEquals(refreshTokenUsing)
            .orElseThrow(() -> new BusinessException(BusinessMessage.NOT_FOUND));
    int userId = currentRefreshToken.getUserId();

    if (currentRefreshToken.getExpiryDate().isBefore(LocalDate.now(ZoneOffset.UTC))) {
      // Refresh token is expired. User must re-login
      this.refreshTokenRepo.deactivateByTokenId(currentRefreshToken.getId());
      throw new BusinessException(BusinessMessage.EXPIRED_REFRESH_TOKEN);
    }

    if (!currentRefreshToken.getIsActive()) {
      // Logout user from all devices
      this.refreshTokenRepo.deleteAllByUserIdEquals(userId);
      throw new BusinessException(BusinessMessage.USING_INACTIVE_REFRESH_TOKEN);
    }

    if (isSpamApiRefreshToken(currentRefreshToken)) {
      throw new BusinessException(BusinessMessage.SPAM_API_REFRESH_TOKEN);
    }

    this.refreshTokenRepo.deactivateByTokenId(currentRefreshToken.getId());
    RefreshToken newRefreshToken = this.create(userId);
    String newJwtToken = this.jwtProvider.generateToken(this.userRepository.findUsernameById(userId));

    return new RefreshTokenResponse(newRefreshToken.getToken(), newJwtToken);
  }

  /**
   * Is spam api refresh token.
   *
   * @param refreshToken the refresh token
   * @return the boolean
   */
  private boolean isSpamApiRefreshToken(@NonNull RefreshToken refreshToken) {
    final OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
    return refreshToken.getCreatedTime().plusSeconds(this.SECONDS_BLOCK_USE_REFRESH_TOKEN).isAfter(now);
  }
}
