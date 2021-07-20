package com.github.mavbraz.timemanager.unit_tests.mapper;

import com.github.mavbraz.timemanager.dto.PersonDTO;
import com.github.mavbraz.timemanager.dto.ProjectDTO;
import com.github.mavbraz.timemanager.dto.TaskDTO;
import com.github.mavbraz.timemanager.dto.enums.TaskStatusDTO;
import com.github.mavbraz.timemanager.entity.Person;
import com.github.mavbraz.timemanager.entity.Project;
import com.github.mavbraz.timemanager.entity.Task;
import com.github.mavbraz.timemanager.entity.enums.TaskStatus;
import com.github.mavbraz.timemanager.helper.ParameterizedTestHelper;
import com.github.mavbraz.timemanager.mapper.TaskMapper;
import com.github.mavbraz.timemanager.mapper.TaskMapperImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TaskMapperImpl.class})
public class TaskMapperTest implements MapperTest<Task, TaskDTO> {

  @Autowired private TaskMapper taskMapper;

  private static Stream<Arguments> provideValidTaskDTO() {
    return Stream.of(
        Arguments.of(
            TaskDTO.builder()
                .id("Task DTO ID 1")
                .description("Task DTO Description 1")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatusDTO.FINISHED)
                .contributors(
                    new ArrayList<>(
                        List.of(
                            PersonDTO.builder()
                                .id("Person DTO ID 1")
                                .name("Person DTO Name 1")
                                .createdAt(LocalDateTime.now())
                                .modifiedAt(LocalDateTime.now())
                                .build())))
                .project(
                    ProjectDTO.builder()
                        .id("Project Entity ID 1")
                        .name("Project Entity Name 1")
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .id("Task DTO ID 2")
                .description("Task DTO Description 2")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatusDTO.FINISHED)
                .contributors(
                    new ArrayList<>(
                        List.of(
                            PersonDTO.builder()
                                .id("Person DTO ID 1")
                                .name("Person DTO Name 1")
                                .createdAt(LocalDateTime.now())
                                .modifiedAt(LocalDateTime.now())
                                .build(),
                            PersonDTO.builder()
                                .id("Person DTO ID 2")
                                .name("Person DTO Name 2")
                                .createdAt(LocalDateTime.now())
                                .modifiedAt(LocalDateTime.now())
                                .build(),
                            PersonDTO.builder()
                                .id("Person DTO ID 3")
                                .name("Person DTO Name 3")
                                .createdAt(LocalDateTime.now())
                                .modifiedAt(LocalDateTime.now())
                                .build())))
                .project(
                    ProjectDTO.builder()
                        .id("Project Entity ID 2")
                        .name("Project Entity Name 2")
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build()));
  }

  private static Stream<Arguments> provideInvalidTaskDTO() {
    return Stream.of(
        Arguments.of(
            TaskDTO.builder()
                .id(null)
                .description(null)
                .startDate(null)
                .finishDate(null)
                .status(null)
                .contributors(new ArrayList<>())
                .project(ProjectDTO.builder().build())
                .createdAt(null)
                .modifiedAt(null)
                .build()),
        Arguments.of(
            TaskDTO.builder()
                .id("")
                .description("")
                .startDate(null)
                .finishDate(null)
                .status(null)
                .contributors(new ArrayList<>())
                .project(ProjectDTO.builder().build())
                .createdAt(null)
                .modifiedAt(null)
                .build()));
  }

  private static Stream<Arguments> provideValidTask() {
    return Stream.of(
        Arguments.of(
            Task.builder()
                .id("Task Entity ID 4")
                .description("Task Entity Name 4")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatus.STARTED)
                .contributors(
                    new ArrayList<>(
                        List.of(
                            Person.builder()
                                .id("Person DTO ID 4")
                                .name("Person DTO Name 4")
                                .createdAt(LocalDateTime.now())
                                .modifiedAt(LocalDateTime.now())
                                .build())))
                .project(
                    Project.builder()
                        .id("Project Entity ID 4")
                        .name("Project Entity Name 4")
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build()),
        Arguments.of(
            Task.builder()
                .id("Task Entity ID 5")
                .description("Task Entity Name 5")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatus.STARTED)
                .contributors(
                    new ArrayList<>(
                        List.of(
                            Person.builder()
                                .id("Person DTO ID 5")
                                .name("Person DTO Name 5")
                                .createdAt(LocalDateTime.now())
                                .modifiedAt(LocalDateTime.now())
                                .build(),
                            Person.builder()
                                .id("Person DTO ID 6")
                                .name("Person DTO Name 6")
                                .createdAt(LocalDateTime.now())
                                .modifiedAt(LocalDateTime.now())
                                .build())))
                .project(
                    Project.builder()
                        .id("Project Entity ID 5")
                        .name("Project Entity Name 5")
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build()),
        Arguments.of(
            Task.builder()
                .id("Task Entity ID 6")
                .description("Task Entity Name 6")
                .startDate(LocalDateTime.now())
                .finishDate(LocalDateTime.now())
                .status(TaskStatus.STARTED)
                .contributors(new ArrayList<>())
                .project(
                    Project.builder()
                        .id("Project Entity ID 6")
                        .name("Project Entity Name 6")
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build()));
  }

  private static Stream<Arguments> provideTasks() {
    return ParameterizedTestHelper.join(provideValidTask());
  }

  private static Stream<Arguments> provideTaskDTOAndTaskToUpdate() {
    return ParameterizedTestHelper.cartesian(provideValidTaskDTO(), provideValidTask());
  }

  @ParameterizedTest
  @MethodSource({"provideValidTaskDTO", "provideInvalidTaskDTO"})
  @Override
  public void Given_DTO_When_ConvertingToEntity_Then_PropertiesShouldBeEquals(TaskDTO taskDTO) {
    // When
    Task task = taskMapper.toEntity(taskDTO);

    // Then
    Assertions.assertNotNull(task);
    Assertions.assertNull(task.getId());
    Assertions.assertEquals(taskDTO.getDescription(), task.getDescription());
    Assertions.assertNull(task.getStartDate());
    Assertions.assertNull(task.getFinishDate());
    Assertions.assertNull(task.getStatus());
    Assertions.assertNull(task.getCreatedAt());
    Assertions.assertNull(task.getModifiedAt());
    Assertions.assertEquals(0L, task.getVersion());

    if (taskDTO.getContributors() != null && task.getContributors() != null) {
      Assertions.assertEquals(taskDTO.getContributors().size(), task.getContributors().size());
      for (int i = 0; i < task.getContributors().size(); i++) {
        PersonDTO personDTO = taskDTO.getContributors().get(i);
        Person person = task.getContributors().get(i);

        Assertions.assertNotNull(personDTO);
        Assertions.assertNotNull(person);
        Assertions.assertEquals(personDTO.getId(), person.getId());
        Assertions.assertEquals(personDTO.getName(), person.getName());
        Assertions.assertEquals(personDTO.getCreatedAt(), person.getCreatedAt());
        Assertions.assertEquals(personDTO.getModifiedAt(), person.getModifiedAt());
      }
    }

    if (taskDTO.getProject() != null && task.getProject() != null) {
      Assertions.assertEquals(taskDTO.getProject().getId(), task.getProject().getId());
      Assertions.assertEquals(taskDTO.getProject().getName(), task.getProject().getName());
      Assertions.assertEquals(
          taskDTO.getProject().getCreatedAt(), task.getProject().getCreatedAt());
      Assertions.assertEquals(
          taskDTO.getProject().getModifiedAt(), task.getProject().getModifiedAt());
    }
  }

  @Test
  @Override
  public void Given_DTO_When_ConvertingNullToEntity_Then_ReturnsNull() {
    // When
    Task task = taskMapper.toEntity(null);

    // Then
    Assertions.assertNull(task);
  }

  @ParameterizedTest
  @MethodSource("provideValidTask")
  @Override
  public void Given_Entity_When_ConvertingToDTO_Then_PropertiesShouldBeEquals(Task task) {
    // When
    TaskDTO taskDTO = taskMapper.toDTO(task);

    // Then
    Assertions.assertNotNull(taskDTO);
    Assertions.assertEquals(task.getId(), taskDTO.getId());
    Assertions.assertEquals(task.getDescription(), taskDTO.getDescription());
    Assertions.assertNotNull(task.getStartDate());
    Assertions.assertTrue(task.getStartDate().isEqual(taskDTO.getStartDate()));
    Assertions.assertNotNull(task.getFinishDate());
    Assertions.assertTrue(task.getFinishDate().isEqual(taskDTO.getFinishDate()));
    Assertions.assertNotNull(task.getStatus());
    Assertions.assertNotNull(taskDTO.getStatus());
    Assertions.assertEquals(task.getStatus().toString(), taskDTO.getStatus().toString());
    Assertions.assertNotNull(task.getCreatedAt());
    Assertions.assertTrue(task.getCreatedAt().isEqual(taskDTO.getCreatedAt()));
    Assertions.assertNotNull(task.getModifiedAt());
    Assertions.assertNotNull(taskDTO.getModifiedAt());
    Assertions.assertTrue(task.getModifiedAt().isEqual(taskDTO.getModifiedAt()));
  }

  @Test
  @Override
  public void Given_Entity_When_ConvertingNullToDTO_Then_ReturnsNull() {
    // When
    TaskDTO taskDTO = taskMapper.toDTO(null);

    // Then
    Assertions.assertNull(taskDTO);
  }

  @ParameterizedTest
  @MethodSource("provideTasks")
  @Override
  public void Given_Entities_When_ConvertingToDTOs_Then_PropertiesShouldBeEquals(List<Task> tasks) {
    // When
    List<TaskDTO> DTOs = taskMapper.mapToDTO(tasks);

    // Then
    Assertions.assertNotNull(DTOs);
    Assertions.assertEquals(tasks.size(), DTOs.size());

    for (int i = 0; i < DTOs.size(); i++) {
      Task task = tasks.get(i);
      TaskDTO taskDTO = DTOs.get(i);

      Assertions.assertEquals(task.getId(), taskDTO.getId());
      Assertions.assertEquals(task.getDescription(), taskDTO.getDescription());
      Assertions.assertNotNull(task.getStartDate());
      Assertions.assertTrue(task.getStartDate().isEqual(taskDTO.getStartDate()));
      Assertions.assertNotNull(task.getFinishDate());
      Assertions.assertTrue(task.getFinishDate().isEqual(taskDTO.getFinishDate()));
      Assertions.assertNotNull(task.getStatus());
      Assertions.assertNotNull(taskDTO.getStatus());
      Assertions.assertEquals(task.getStatus().toString(), taskDTO.getStatus().toString());
      Assertions.assertNotNull(task.getCreatedAt());
      Assertions.assertNotNull(taskDTO.getCreatedAt());
      Assertions.assertTrue(task.getCreatedAt().isEqual(taskDTO.getCreatedAt()));
      Assertions.assertNotNull(task.getModifiedAt());
      Assertions.assertNotNull(taskDTO.getModifiedAt());
      Assertions.assertTrue(task.getModifiedAt().isEqual(taskDTO.getModifiedAt()));
    }
  }

  @Test
  @Override
  public void Given_Entities_When_ConvertingNullToDTOs_Then_ReturnsNull() {
    // When
    List<TaskDTO> DTOs = taskMapper.mapToDTO(null);

    // Then
    Assertions.assertNull(DTOs);
  }

  @ParameterizedTest
  @MethodSource("provideTaskDTOAndTaskToUpdate")
  @Override
  public void Given_DTOAndEntity_When_UpdatingEntityByDTO_Then_PropertiesShouldBeEquals(
      TaskDTO taskDTO, Task task) {
    // Given
    List<PersonDTO> assertionContributors =
        taskDTO.getContributors().stream()
            .map(p -> p.toBuilder().build())
            .collect(Collectors.toList());
    ProjectDTO assertionProject = taskDTO.getProject().toBuilder().build();
    Task assertionTask = task.toBuilder().build();
    assertionTask.setContributors(null);
    assertionTask.setProject(null);

    // When
    taskMapper.update(taskDTO, task);

    // Then
    Assertions.assertEquals(assertionTask.getId(), task.getId());
    Assertions.assertNotNull(task.getDescription());
    Assertions.assertNotEquals(assertionTask.getDescription(), task.getDescription());
    Assertions.assertNotNull(assertionTask.getStartDate());
    Assertions.assertNotNull(task.getStartDate());
    Assertions.assertTrue(assertionTask.getStartDate().isEqual(task.getStartDate()));
    Assertions.assertNotNull(assertionTask.getFinishDate());
    Assertions.assertNotNull(task.getFinishDate());
    Assertions.assertTrue(assertionTask.getFinishDate().isEqual(task.getFinishDate()));
    Assertions.assertNotNull(assertionTask.getStatus());
    Assertions.assertNotNull(task.getStatus().toString());
    Assertions.assertEquals(assertionTask.getStatus().toString(), task.getStatus().toString());
    Assertions.assertNotNull(assertionTask.getCreatedAt());
    Assertions.assertNotNull(task.getCreatedAt());
    Assertions.assertTrue(assertionTask.getCreatedAt().isEqual(task.getCreatedAt()));
    Assertions.assertNotNull(assertionTask.getModifiedAt());
    Assertions.assertNotNull(task.getModifiedAt());
    Assertions.assertTrue(assertionTask.getModifiedAt().isEqual(task.getModifiedAt()));
    Assertions.assertEquals(assertionTask.getVersion(), task.getVersion());

    Assertions.assertEquals(assertionContributors.size(), task.getContributors().size());
    for (int i = 0; i < task.getContributors().size(); i++) {
      PersonDTO assertionPerson = assertionContributors.get(i);
      Person person = task.getContributors().get(i);

      Assertions.assertNotNull(assertionPerson);
      Assertions.assertNotNull(person);
      Assertions.assertEquals(assertionPerson.getId(), person.getId());
      Assertions.assertEquals(assertionPerson.getName(), person.getName());
      Assertions.assertEquals(assertionPerson.getCreatedAt(), person.getCreatedAt());
      Assertions.assertEquals(assertionPerson.getModifiedAt(), person.getModifiedAt());
      Assertions.assertEquals(0L, person.getVersion());
    }

    Assertions.assertNotNull(task.getProject());
    Assertions.assertEquals(assertionProject.getId(), task.getProject().getId());
    Assertions.assertEquals(assertionProject.getName(), task.getProject().getName());
    Assertions.assertNotNull(assertionProject.getCreatedAt());
    Assertions.assertNotNull(task.getProject().getCreatedAt());
    Assertions.assertTrue(
        assertionProject.getCreatedAt().isEqual(task.getProject().getCreatedAt()));
    Assertions.assertNotNull(assertionProject.getModifiedAt());
    Assertions.assertNotNull(task.getProject().getModifiedAt());
    Assertions.assertTrue(
        assertionProject.getModifiedAt().isEqual(task.getProject().getModifiedAt()));
    Assertions.assertEquals(0L, task.getProject().getVersion());
  }

  @ParameterizedTest
  @MethodSource("provideValidTask")
  @Override
  public void Given_DTOAndEntity_When_UpdatingEntityByInvalidDTO_Then_PropertiesShouldNotBeEquals(
      TaskDTO taskDTO, Task task) {
    // Given
    List<Person> assertionContributors =
        task.getContributors().stream()
            .map(p -> p.toBuilder().build())
            .collect(Collectors.toList());
    Project assertionProject = task.getProject().toBuilder().build();
    Task assertionTask = task.toBuilder().build();
    assertionTask.setContributors(null);
    assertionTask.setProject(null);

    // When
    taskMapper.update(null, task);

    // Then
    Assertions.assertEquals(assertionTask.getId(), task.getId());
    Assertions.assertEquals(assertionTask.getDescription(), task.getDescription());
    Assertions.assertNotNull(assertionTask.getStartDate());
    Assertions.assertNotNull(task.getStartDate());
    Assertions.assertTrue(assertionTask.getStartDate().isEqual(task.getStartDate()));
    Assertions.assertNotNull(assertionTask.getFinishDate());
    Assertions.assertNotNull(task.getFinishDate());
    Assertions.assertTrue(assertionTask.getFinishDate().isEqual(task.getFinishDate()));
    Assertions.assertNotNull(assertionTask.getStatus());
    Assertions.assertNotNull(task.getStatus().toString());
    Assertions.assertEquals(assertionTask.getStatus().toString(), task.getStatus().toString());
    Assertions.assertNotNull(assertionTask.getCreatedAt());
    Assertions.assertNotNull(task.getCreatedAt());
    Assertions.assertTrue(assertionTask.getCreatedAt().isEqual(task.getCreatedAt()));
    Assertions.assertNotNull(assertionTask.getModifiedAt());
    Assertions.assertNotNull(task.getModifiedAt());
    Assertions.assertTrue(assertionTask.getModifiedAt().isEqual(task.getModifiedAt()));
    Assertions.assertEquals(assertionTask.getVersion(), task.getVersion());

    Assertions.assertEquals(assertionContributors.size(), task.getContributors().size());
    for (int i = 0; i < task.getContributors().size(); i++) {
      Person assertionPerson = assertionContributors.get(i);
      Person person = task.getContributors().get(i);

      Assertions.assertNotNull(assertionPerson);
      Assertions.assertNotNull(person);
      Assertions.assertEquals(assertionPerson.getId(), person.getId());
      Assertions.assertEquals(assertionPerson.getName(), person.getName());
      Assertions.assertEquals(assertionPerson.getCreatedAt(), person.getCreatedAt());
      Assertions.assertEquals(assertionPerson.getModifiedAt(), person.getModifiedAt());
      Assertions.assertEquals(assertionPerson.getVersion(), person.getVersion());
    }

    Assertions.assertNotNull(task.getProject());
    Assertions.assertEquals(assertionProject.getId(), task.getProject().getId());
    Assertions.assertEquals(assertionProject.getName(), task.getProject().getName());
    Assertions.assertNotNull(assertionProject.getCreatedAt());
    Assertions.assertNotNull(task.getProject().getCreatedAt());
    Assertions.assertTrue(
        assertionProject.getCreatedAt().isEqual(task.getProject().getCreatedAt()));
    Assertions.assertNotNull(assertionProject.getModifiedAt());
    Assertions.assertNotNull(task.getProject().getModifiedAt());
    Assertions.assertTrue(
        assertionProject.getModifiedAt().isEqual(task.getProject().getModifiedAt()));
    Assertions.assertEquals(assertionProject.getVersion(), task.getProject().getVersion());
  }
}
