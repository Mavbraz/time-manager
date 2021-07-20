package com.github.mavbraz.timemanager.entity;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;

/**
 * Base for create entity.
 *
 * @see Data
 * @see EqualsAndHashCode
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder(toBuilder = true)
public abstract class BaseDocument implements Persistable<String> {

  @Id @EqualsAndHashCode.Include private String id;
  @CreatedDate private LocalDateTime createdAt;
  @LastModifiedDate private LocalDateTime modifiedAt;
  @Version private long version;

  protected BaseDocument() {}

  /**
   * Returns the entity id for processing during persistence.
   *
   * @return the entity id
   */
  @Override
  public String getId() {
    return id;
  }

  /**
   * Returns if the entity is newer or older during persistence.
   *
   * @return the status
   */
  @Override
  public boolean isNew() {
    return Optional.ofNullable(getId()).map(String::isBlank).orElse(true);
  }
}
