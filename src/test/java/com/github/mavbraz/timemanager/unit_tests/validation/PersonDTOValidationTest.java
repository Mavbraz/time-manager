package com.github.mavbraz.timemanager.unit_tests.validation;

import com.github.mavbraz.timemanager.dto.PersonDTO;
import com.github.mavbraz.timemanager.helper.model.ConstraintViolationData;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.validation.ConstraintViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PersonDTOValidationTest extends BaseDTOValidationTest<PersonDTO> {

  private static Stream<Arguments> provideValidPersonDTO() {
    return Stream.of(
        Arguments.of(PersonDTO.builder().name("DTO Name").build()),
        Arguments.of(PersonDTO.builder().name("   a ").build()));
  }

  private static Stream<Arguments> provideInvalidPersonDTO() {
    ConstraintViolationData<PersonDTO> constraintViolationData =
        ConstraintViolationData.<PersonDTO>builder()
            .propertyPath("name")
            .invalidValue(PersonDTO::getName)
            .message("must not be blank")
            .messageTemplate("{javax.validation.constraints.NotBlank.message}")
            .build();

    return Stream.of(
        Arguments.of(PersonDTO.builder().name(null).build(), constraintViolationData),
        Arguments.of(PersonDTO.builder().name("").build(), constraintViolationData),
        Arguments.of(PersonDTO.builder().name("    ").build(), constraintViolationData));
  }

  @ParameterizedTest
  @MethodSource("provideValidPersonDTO")
  @Override
  public void Given_ValidDTO_When_Validating_Then_Success(PersonDTO personDTO) {
    // When
    List<ConstraintViolation<PersonDTO>> violations =
        new ArrayList<>(validator.validate(personDTO));

    // Then
    Assertions.assertTrue(violations.isEmpty());
  }

  @ParameterizedTest
  @MethodSource("provideInvalidPersonDTO")
  @Override
  public void Given_InvalidDTO_When_Validating_Then_Error(
      PersonDTO personDTO, ConstraintViolationData<PersonDTO> constraintViolationData) {
    // When
    List<ConstraintViolation<PersonDTO>> violations =
        new ArrayList<>(validator.validate(personDTO));

    // Then
    Assertions.assertFalse(violations.isEmpty());
    Assertions.assertEquals(1, violations.size());

    ConstraintViolation<PersonDTO> nameViolation = violations.get(0);

    Assertions.assertEquals(
        constraintViolationData.getPropertyPath(), nameViolation.getPropertyPath().toString());
    Assertions.assertEquals(
        constraintViolationData.getInvalidValue().apply(personDTO),
        nameViolation.getInvalidValue());
    Assertions.assertEquals(constraintViolationData.getMessage(), nameViolation.getMessage());
    Assertions.assertEquals(
        constraintViolationData.getMessageTemplate(), nameViolation.getMessageTemplate());
  }
}
