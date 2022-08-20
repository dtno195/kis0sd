package com.sd.dto.user;

import com.sd.common.enums.Authority;
import com.sd.common.enums.Language;
import com.sd.entity.User;
import lombok.*;

/**
 * @author Dustin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
  /**
   * The Id.
   */
  private int id;

  /**
   * The Username.
   */
  private String username;

  /**
   * The Password.
   */
  private String password;

  /**
   * The Password Confirmed.
   */
  private String passwordConfirmed;

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
   * Convert user request to user entity.
   *
   * @param dto the user request
   * @return the user entity
   */
  public static User toEntity(@NonNull UserRequest dto) {
    return User.builder()
            .id(dto.getId())
            .username(dto.getUsername())
            .password(dto.getPassword())
            .authority(dto.getAuthority())
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .address(dto.getAddress())
            .phone(dto.getPhone())
            .mail(dto.getMail())
            .language(dto.getLanguage())
            .build();
  }
}
