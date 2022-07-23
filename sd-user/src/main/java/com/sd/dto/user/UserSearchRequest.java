package com.sd.dto.user;

import com.sd.common.enums.Authority;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Dustin
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSearchRequest {
  String keySearch;
  Authority authority;
}
