package com.sd.dto.user;

import com.sd.common.enums.Authority;
import com.sd.common.enums.Language;
import com.sd.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * @author Dustin
 */
@Data
@Builder
public class UserResponse {
  /**
   * The Id.
   */
  private long id;

  /**
   * The Username.
   */
  private String username;

  /**
   * The Authority.
   */
  private Authority authority;

  /**
   * The First name.
   */
  private String firstName;

  /**
   * The Last name.
   */
  private String lastName;

  /**
   * The Address.
   */
  private String address;

  /**
   * The Phone.
   */
  private String phone;

  /**
   * The Mail.
   */
  private String mail;

  /**
   * The Language.
   */
  private Language language;

  /**
   * The Last login time.
   */
  private OffsetDateTime lastLoginTime;

  /**
   * The Created time.
   */
  private OffsetDateTime createdTime;

  /**
   * The Updated time.
   */
  private OffsetDateTime updatedTime;

  private String fullName;

  /**
   * Gets user response from user entity.
   *
   * @param entity the user entity
   * @return the user response
   */
  public static UserResponse fromEntity(@NonNull User entity) {
    return UserResponse.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .authority(entity.getAuthority())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .address(entity.getAddress())
            .phone(entity.getPhone())
            .mail(entity.getMail())
            .language(entity.getLanguage())
            .lastLoginTime(entity.getLastLoginTime())
            .createdTime(entity.getCreatedTime())
            .updatedTime(entity.getUpdatedTime())
            .fullName((Objects.isNull(entity.getFirstName()) ? "" : entity.getFirstName()) + " " + (Objects.isNull(entity.getLastName()) ? "" : entity.getLastName()))
            .build();
  }
}
