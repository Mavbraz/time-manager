package com.github.mavbraz.timemanager.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * DTO class used for map the Person.
 *
 * @see Data
 * @see EqualsAndHashCode
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class PersonDTO extends BaseDTO {

  @NotBlank private String name;

  public PersonDTO() {}
}
