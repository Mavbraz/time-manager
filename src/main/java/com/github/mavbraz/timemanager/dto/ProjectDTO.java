package com.github.mavbraz.timemanager.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * DTO class used for map the Project.
 *
 * @see Data
 * @see EqualsAndHashCode
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class ProjectDTO extends BaseDTO {

  @NotBlank private String name;

  public ProjectDTO() {}
}
