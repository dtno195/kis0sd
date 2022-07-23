package com.sd.entity.base;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * @author Dustin
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@FieldNameConstants
@MappedSuperclass
public class BaseEntity {
  /**
   * The Created time.
   */
  @Column(name = "created_time", nullable = false)
  private OffsetDateTime createdTime;

  /**
   * The Updated time.
   */
  @Column(name = "updated_time")
  private OffsetDateTime updatedTime;

  /**
   * Pre create.
   */
  @PrePersist
  private void prePersist() {
    this.createdTime = OffsetDateTime.now(ZoneOffset.UTC);
  }

  /**
   * Pre update.
   */
  @PreUpdate
  private void preUpdate() {
    this.updatedTime = OffsetDateTime.now(ZoneOffset.UTC);
  }
}
