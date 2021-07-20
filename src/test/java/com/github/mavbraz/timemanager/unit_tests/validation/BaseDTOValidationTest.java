package com.github.mavbraz.timemanager.unit_tests.validation;

import com.github.mavbraz.timemanager.dto.BaseDTO;
import com.github.mavbraz.timemanager.helper.model.ConstraintViolationData;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseDTOValidationTest<D extends BaseDTO> {

  protected static ValidatorFactory validatorFactory;
  protected static Validator validator;

  @BeforeAll
  public static void setUp() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @AfterAll
  public static void close() {
    validatorFactory.close();
  }

  abstract void Given_ValidDTO_When_Validating_Then_Success(D dto);

  abstract void Given_InvalidDTO_When_Validating_Then_Error(
      D dto, ConstraintViolationData<D> constraintViolationData);
}
