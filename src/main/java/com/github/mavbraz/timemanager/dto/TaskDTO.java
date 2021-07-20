package com.github.mavbraz.timemanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.mavbraz.timemanager.dto.enums.TaskStatusDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * DTO class used for map the Task.
 *
 * @see Data
 * @see EqualsAndHashCode
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class TaskDTO extends BaseDTO {

  @NotBlank private String description;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  @PastOrPresent
  private LocalDateTime startDate;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  @PastOrPresent
  private LocalDateTime finishDate;

  @NotNull private TaskStatusDTO status;
  @NotEmpty @Valid private List<PersonDTO> contributors = new ArrayList<>();
  @NotNull @Valid private ProjectDTO project;

  public TaskDTO() {}
}
