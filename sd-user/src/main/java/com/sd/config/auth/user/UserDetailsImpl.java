package com.sd.config.auth.user;

import com.sd.common.enums.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Dustin
 */
@RequiredArgsConstructor
public final class UserDetailsImpl implements UserDetails {

  /**
   * The Id.
   */
  private final long id;

  /**
   * The Username.
   */
  private final String username;

  /**
   * The Password.
   */
  private final String password;

  /**
   * The Authority.
   */
  private final Authority authority;


  /**
   * Gets id.
   *
   * @return the id
   */
  public long getId() {
    return this.id;
  }

  /**
   * Gets authorities.
   *
   * @return the authorities
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(this.authority.name()));
  }

  /**
   * Gets password.
   *
   * @return the password
   */
  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
