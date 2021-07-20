package com.github.mavbraz.timemanager.unit_tests.validation;

import com.github.mavbraz.timemanager.dto.PersonDTO;
import com.github.mavbraz.timemanager.dto.ProjectDTO;
import com.github.mavbraz.timemanager.dto.TaskDTO;
import com.github.mavbraz.timemanager.dto.enums.TaskStatusDTO;
import com.github.mavbraz.timemanager.helper.model.ConstraintViolationData;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.validation.ConstraintViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TaskDTOValidationTest extends BaseDTOValidationTest<TaskDTO> {

  private static Stream<Arguments> provideValidPersonDTO() {
    return Stream.of(
        Arguments.of(
            TaskDTO.builder()
                .description("Task DTO Name 1")
                .startDate(null)
                .finishDate(null)
                .status(TaskStatusDTO.NOT_STARTED)
                .contributors(List.of(PersonDTO.builder().name("Person DTO Name 1").build()))
                .project(ProjectDTO.builder().name("Project DTO Name 1").build())
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("Task DTO Name 2   ")
                .startDate(LocalDateTime.now())
                .finishDate(null)
                .status(TaskStatusDTO.STARTED)
                .contributors(List.of(PersonDTO.builder().name("Person DTO Name 2   ").build()))
                .project(ProjectDTO.builder().name("Project DTO Name 2   ").build())
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("  Task DTO Name 3 ")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatusDTO.FINISHED)
                .contributors(List.of(PersonDTO.builder().name("   Person DTO Name 3 ").build()))
                .project(ProjectDTO.builder().name("   Project DTO Name 3 ").build())
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("  Task DTO Name 4 ")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatusDTO.FINISHED)
                .contributors(
                    List.of(
                        PersonDTO.builder().name("   Person DTO Name 4 ").build(),
                        PersonDTO.builder().name("   Person DTO Name 5 ").build()))
                .project(ProjectDTO.builder().name("   Project DTO Name 4 ").build())
                .build()));
  }

  private static Stream<Arguments> provideInvalidPersonDTO() {
    return Stream.of(
        Arguments.of(
            TaskDTO.builder()
                .description(null)
                .startDate(null)
                .finishDate(null)
                .status(TaskStatusDTO.NOT_STARTED)
                .contributors(List.of(PersonDTO.builder().name("Person DTO Name 1").build()))
                .project(ProjectDTO.builder().name("Project DTO Name 1").build())
                .build(),
            ConstraintViolationData.<TaskDTO>builder()
                .propertyPath("description")
                .invalidValue(TaskDTO::getDescription)
                .message("must not be blank")
                .messageTemplate("{javax.validation.constraints.NotBlank.message}")
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("   ")
                .startDate(null)
                .finishDate(null)
                .status(TaskStatusDTO.NOT_STARTED)
                .contributors(List.of(PersonDTO.builder().name("Person DTO Name 2").build()))
                .project(ProjectDTO.builder().name("Project DTO Name 2").build())
                .build(),
            ConstraintViolationData.<TaskDTO>builder()
                .propertyPath("description")
                .invalidValue(TaskDTO::getDescription)
                .message("must not be blank")
                .messageTemplate("{javax.validation.constraints.NotBlank.message}")
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("Task DTO Name 3")
                .startDate(LocalDateTime.now())
                .status(null)
                .contributors(List.of(PersonDTO.builder().name("Person DTO Name 3").build()))
                .project(ProjectDTO.builder().name("Project DTO Name 3").build())
                .build(),
            ConstraintViolationData.<TaskDTO>builder()
                .propertyPath("status")
                .invalidValue(TaskDTO::getStatus)
                .message("must not be null")
                .messageTemplate("{javax.validation.constraints.NotNull.message}")
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("Person DTO Name 4")
                .startDate(LocalDateTime.now().plusHours(12))
                .status(TaskStatusDTO.STARTED)
                .contributors(List.of(PersonDTO.builder().name("Person DTO Name 4").build()))
                .project(ProjectDTO.builder().name("Project DTO Name 4").build())
                .build(),
            ConstraintViolationData.<TaskDTO>builder()
                .propertyPath("startDate")
                .invalidValue(TaskDTO::getStartDate)
                .message("must be a date in the past or in the present")
                .messageTemplate("{javax.validation.constraints.PastOrPresent.message}")
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("Person DTO Name 5")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now().plusHours(12))
                .status(TaskStatusDTO.FINISHED)
                .contributors(List.of(PersonDTO.builder().name("Person DTO Name 5").build()))
                .project(ProjectDTO.builder().name("Project DTO Name 5").build())
                .build(),
            ConstraintViolationData.<TaskDTO>builder()
                .propertyPath("finishDate")
                .invalidValue(TaskDTO::getFinishDate)
                .message("must be a date in the past or in the present")
                .messageTemplate("{javax.validation.constraints.PastOrPresent.message}")
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("Person DTO Name 6")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatusDTO.FINISHED)
                .contributors(List.of(PersonDTO.builder().name(null).build()))
                .project(ProjectDTO.builder().name("Project DTO Name 6").build())
                .build(),
            ConstraintViolationData.<TaskDTO>builder()
                .propertyPath("contributors[0].name")
                .invalidValue(t -> t.getContributors().get(0).getName())
                .message("must not be blank")
                .messageTemplate("{javax.validation.constraints.NotBlank.message}")
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("Person DTO Name 7")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatusDTO.FINISHED)
                .contributors(List.of(PersonDTO.builder().name("    ").build()))
                .project(ProjectDTO.builder().name("Project DTO Name 7").build())
                .build(),
            ConstraintViolationData.<TaskDTO>builder()
                .propertyPath("contributors[0].name")
                .invalidValue(t -> t.getContributors().get(0).getName())
                .message("must not be blank")
                .messageTemplate("{javax.validation.constraints.NotBlank.message}")
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("Person DTO Name 8")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatusDTO.FINISHED)
                .contributors(null)
                .project(ProjectDTO.builder().name("Project DTO Name 8").build())
                .build(),
            ConstraintViolationData.<TaskDTO>builder()
                .propertyPath("contributors")
                .invalidValue(TaskDTO::getContributors)
                .message("must not be empty")
                .messageTemplate("{javax.validation.constraints.NotEmpty.message}")
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("Person DTO Name 9")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatusDTO.FINISHED)
                .contributors(new ArrayList<>())
                .project(ProjectDTO.builder().name("Project DTO Name 9").build())
                .build(),
            ConstraintViolationData.<TaskDTO>builder()
                .propertyPath("contributors")
                .invalidValue(TaskDTO::getContributors)
                .message("must not be empty")
                .messageTemplate("{javax.validation.constraints.NotEmpty.message}")
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("Person DTO Name 10")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatusDTO.FINISHED)
                .contributors(List.of(PersonDTO.builder().name("Person DTO Name 10").build()))
                .project(ProjectDTO.builder().name(null).build())
                .build(),
            ConstraintViolationData.<TaskDTO>builder()
                .propertyPath("project.name")
                .invalidValue(t -> t.getProject().getName())
                .message("must not be blank")
                .messageTemplate("{javax.validation.constraints.NotBlank.message}")
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("Person DTO Name 11")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatusDTO.FINISHED)
                .contributors(List.of(PersonDTO.builder().name("Person DTO Name 11").build()))
                .project(ProjectDTO.builder().name("    ").build())
                .build(),
            ConstraintViolationData.<TaskDTO>builder()
                .propertyPath("project.name")
                .invalidValue(t -> t.getProject().getName())
                .message("must not be blank")
                .messageTemplate("{javax.validation.constraints.NotBlank.message}")
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .description("Person DTO Name 12")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatusDTO.FINISHED)
                .contributors(List.of(PersonDTO.builder().name("Person DTO Name 12").build()))
                .project(null)
                .build(),
            ConstraintViolationData.<TaskDTO>builder()
                .propertyPath("project")
                .invalidValue(TaskDTO::getProject)
                .message("must not be null")
                .messageTemplate("{javax.validation.constraints.NotNull.message}")
                .build()));
  }

  @ParameterizedTest
  @MethodSource("provideValidPersonDTO")
  @Override
  public void Given_ValidDTO_When_Validating_Then_Success(TaskDTO taskDTO) {
    // When
    List<ConstraintViolation<TaskDTO>> violations = new ArrayList<>(validator.validate(taskDTO));

    // Then
    Assertions.assertTrue(violations.isEmpty());
  }

  @ParameterizedTest
  @MethodSource("provideInvalidPersonDTO")
  @Override
  public void Given_InvalidDTO_When_Validating_Then_Error(
      TaskDTO taskDTO, ConstraintViolationData<TaskDTO> constraintViolationData) {
    // When
    List<ConstraintViolation<TaskDTO>> violations = new ArrayList<>(validator.validate(taskDTO));

    // Then
    Assertions.assertFalse(violations.isEmpty());
    Assertions.assertEquals(1, violations.size());

    ConstraintViolation<TaskDTO> nameViolation = violations.get(0);

    Assertions.assertEquals(
        constraintViolationData.getPropertyPath(), nameViolation.getPropertyPath().toString());
    Assertions.assertEquals(
        constraintViolationData.getInvalidValue().apply(taskDTO), nameViolation.getInvalidValue());
    Assertions.assertEquals(constraintViolationData.getMessage(), nameViolation.getMessage());
    Assertions.assertEquals(
        constraintViolationData.getMessageTemplate(), nameViolation.getMessageTemplate());
  }
}
