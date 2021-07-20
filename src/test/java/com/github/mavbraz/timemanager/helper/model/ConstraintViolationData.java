package com.github.mavbraz.timemanager.helper.model;

import java.util.function.Function;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConstraintViolationData<D> {

  private String propertyPath;
  private Function<D, Object> invalidValue;
  private String message;
  private String messageTemplate;
}
