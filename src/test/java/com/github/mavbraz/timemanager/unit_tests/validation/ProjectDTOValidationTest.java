package com.github.mavbraz.timemanager.unit_tests.validation;

import com.github.mavbraz.timemanager.dto.ProjectDTO;
import com.github.mavbraz.timemanager.helper.model.ConstraintViolationData;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.validation.ConstraintViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ProjectDTOValidationTest extends BaseDTOValidationTest<ProjectDTO> {

  private static Stream<Arguments> provideValidProjectDTO() {
    return Stream.of(
        Arguments.of(ProjectDTO.builder().name("DTO Name").build()),
        Arguments.of(ProjectDTO.builder().name("   a ").build()));
  }

  private static Stream<Arguments> provideInvalidProjectDTO() {
    ConstraintViolationData<ProjectDTO> constraintViolationData =
        ConstraintViolationData.<ProjectDTO>builder()
            .propertyPath("name")
            .invalidValue(ProjectDTO::getName)
            .message("must not be blank")
            .messageTemplate("{javax.validation.constraints.NotBlank.message}")
            .build();

    return Stream.of(
        Arguments.of(ProjectDTO.builder().name(null).build(), constraintViolationData),
        Arguments.of(ProjectDTO.builder().name("").build(), constraintViolationData),
        Arguments.of(ProjectDTO.builder().name("    ").build(), constraintViolationData));
  }

  @ParameterizedTest
  @MethodSource("provideValidProjectDTO")
  public void Given_ValidDTO_When_Validating_Then_Success(ProjectDTO projectDTO) {
    // When
    List<ConstraintViolation<ProjectDTO>> violations =
        new ArrayList<>(validator.validate(projectDTO));

    // Then
    Assertions.assertTrue(violations.isEmpty());
  }

  @ParameterizedTest
  @MethodSource("provideInvalidProjectDTO")
  public void Given_InvalidDTO_When_Validating_Then_Error(
      ProjectDTO projectDTO, ConstraintViolationData<ProjectDTO> constraintViolationData) {
    // When
    List<ConstraintViolation<ProjectDTO>> violations =
        new ArrayList<>(validator.validate(projectDTO));

    // Then
    Assertions.assertFalse(violations.isEmpty());
    Assertions.assertEquals(1, violations.size());

    ConstraintViolation<ProjectDTO> nameViolation = violations.get(0);

    Assertions.assertEquals(
        constraintViolationData.getPropertyPath(), nameViolation.getPropertyPath().toString());
    Assertions.assertEquals(
        constraintViolationData.getInvalidValue().apply(projectDTO),
        nameViolation.getInvalidValue());
    Assertions.assertEquals(constraintViolationData.getMessage(), nameViolation.getMessage());
    Assertions.assertEquals(
        constraintViolationData.getMessageTemplate(), nameViolation.getMessageTemplate());
  }
}
