package com.sd.dto.user;

import com.sd.common.enums.Authority;
import com.sd.common.http.PagingRequest;
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
public class UserSearchRequest extends PagingRequest {

  String keySearch;

  Authority authority;
}
