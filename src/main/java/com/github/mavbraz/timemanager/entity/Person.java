package com.github.mavbraz.timemanager.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class used for map the Person.
 *
 * @see Document
 * @see Data
 * @see EqualsAndHashCode
 */
@Document
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class Person extends BaseDocument {

  private String name;

  public Person() {}
}
