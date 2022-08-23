package com.sd.dto.user.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Dustin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
  /**
   * The Username.
   */
  @NotBlank(message = "message.field_not_blank")
  private String username;

  /**
   * The Password.
   */
  @NotBlank(message = "message.field_not_blank")
  private String password;
}
