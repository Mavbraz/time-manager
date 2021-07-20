package com.github.mavbraz.timemanager.entity;

import com.github.mavbraz.timemanager.entity.enums.TaskStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class used for map the Task.
 *
 * @see Document
 * @see Data
 * @see EqualsAndHashCode
 */
@Document
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class Task extends BaseDocument {

  private String description;
  private LocalDateTime startDate;
  private LocalDateTime finishDate;
  private TaskStatus status = TaskStatus.NOT_STARTED;
  @DBRef private List<Person> contributors = new ArrayList<>();
  @DBRef private Project project;

  public Task() {}
}
