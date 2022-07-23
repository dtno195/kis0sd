package com.sd.entity;

import com.sd.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Dustin
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
@Table(name = "tbl_refresh_token")
@Entity
public class RefreshToken extends BaseEntity implements Serializable {
  /**
   * The Id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**
   * The Token.
   */
  @Column(name = "token", length = 36, nullable = false, unique = true)
  private String token;

  /**
   * The User id.
   */
  @Column(name = "user_id", nullable = false)
  private int userId;

  /**
   * The Is active.
   */
  @Column(name = "is_active")
  private Boolean isActive;

  /**
   * The Expiry date.
   */
  @Column(name = "expiry_date", nullable = false)
  private LocalDate expiryDate;
}
