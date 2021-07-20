package com.github.mavbraz.timemanager.unit_tests.mapper;

import com.github.mavbraz.timemanager.dto.ProjectDTO;
import com.github.mavbraz.timemanager.entity.Project;
import com.github.mavbraz.timemanager.helper.ParameterizedTestHelper;
import com.github.mavbraz.timemanager.mapper.ProjectMapper;
import com.github.mavbraz.timemanager.mapper.ProjectMapperImpl;
import java.time.LocalDateTime;
import java.util.List;
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
@ContextConfiguration(classes = {ProjectMapperImpl.class})
public class ProjectMapperTest implements MapperTest<Project, ProjectDTO> {

  @Autowired private ProjectMapper projectMapper;

  private static Stream<Arguments> provideValidProjectDTO() {
    return Stream.of(
        Arguments.of(
            ProjectDTO.builder()
                .id("DTO 1")
                .name("DTO Name 1")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build()));
  }

  private static Stream<Arguments> provideInvalidProjectDTO() {
    return Stream.of(
        Arguments.of(
            ProjectDTO.builder().id(null).name(null).createdAt(null).modifiedAt(null).build()),
        Arguments.of(
            ProjectDTO.builder().id("").name("").createdAt(null).modifiedAt(null).build()));
  }

  private static Stream<Arguments> provideValidProject() {
    return Stream.of(
        Arguments.of(
            Project.builder()
                .id("Entity 2")
                .name("Entity Name 2")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .version(1)
                .build()));
  }

  private static Stream<Arguments> provideProjects() {
    return ParameterizedTestHelper.join(provideValidProject());
  }

  private static Stream<Arguments> provideProjectDTOAndProjectToUpdate() {
    return ParameterizedTestHelper.cartesian(provideValidProjectDTO(), provideValidProject());
  }

  private static Stream<Arguments> provideInvalidProjectDTOAndProjectToUpdate() {
    return ParameterizedTestHelper.cartesian(
        Stream.of(Arguments.of((ProjectDTO) null)), provideValidProject());
  }

  @ParameterizedTest
  @MethodSource({"provideValidProjectDTO", "provideInvalidProjectDTO"})
  @Override
  public void Given_DTO_When_ConvertingToEntity_Then_PropertiesShouldBeEquals(
      ProjectDTO projectDTO) {
    // When
    Project project = projectMapper.toEntity(projectDTO);

    // Then
    Assertions.assertNotNull(project);
    Assertions.assertNull(project.getId());
    Assertions.assertEquals(projectDTO.getName(), project.getName());
    Assertions.assertNull(project.getCreatedAt());
    Assertions.assertNull(project.getModifiedAt());
    Assertions.assertEquals(0L, project.getVersion());
  }

  @Test
  @Override
  public void Given_DTO_When_ConvertingNullToEntity_Then_ReturnsNull() {
    // When
    Project project = projectMapper.toEntity(null);

    // Then
    Assertions.assertNull(project);
  }

  @ParameterizedTest
  @MethodSource("provideValidProject")
  @Override
  public void Given_Entity_When_ConvertingToDTO_Then_PropertiesShouldBeEquals(Project project) {
    // When
    ProjectDTO projectDTO = projectMapper.toDTO(project);

    // Then
    Assertions.assertNotNull(projectDTO);
    Assertions.assertEquals(project.getId(), projectDTO.getId());
    Assertions.assertEquals(project.getName(), projectDTO.getName());
    Assertions.assertNotNull(project.getCreatedAt());
    Assertions.assertNotNull(projectDTO.getCreatedAt());
    Assertions.assertTrue(project.getCreatedAt().isEqual(projectDTO.getCreatedAt()));
    Assertions.assertNotNull(project.getModifiedAt());
    Assertions.assertNotNull(projectDTO.getModifiedAt());
    Assertions.assertTrue(project.getModifiedAt().isEqual(projectDTO.getModifiedAt()));
  }

  @Test
  @Override
  public void Given_Entity_When_ConvertingNullToDTO_Then_ReturnsNull() {
    // When
    ProjectDTO projectDTO = projectMapper.toDTO(null);

    // Then
    Assertions.assertNull(projectDTO);
  }

  @ParameterizedTest
  @MethodSource("provideProjects")
  @Override
  public void Given_Entities_When_ConvertingToDTOs_Then_PropertiesShouldBeEquals(
      List<Project> projects) {
    // When
    List<ProjectDTO> DTOs = projectMapper.mapToDTO(projects);

    // Then
    Assertions.assertNotNull(DTOs);
    Assertions.assertEquals(projects.size(), DTOs.size());

    for (int i = 0; i < DTOs.size(); i++) {
      Project project = projects.get(i);
      ProjectDTO projectDTO = DTOs.get(i);

      Assertions.assertEquals(project.getId(), projectDTO.getId());
      Assertions.assertEquals(project.getName(), projectDTO.getName());
      Assertions.assertNotNull(project.getCreatedAt());
      Assertions.assertNotNull(projectDTO.getCreatedAt());
      Assertions.assertTrue(project.getCreatedAt().isEqual(projectDTO.getCreatedAt()));
      Assertions.assertNotNull(project.getModifiedAt());
      Assertions.assertNotNull(projectDTO.getModifiedAt());
      Assertions.assertTrue(project.getModifiedAt().isEqual(projectDTO.getModifiedAt()));
    }
  }

  @Test
  @Override
  public void Given_Entities_When_ConvertingNullToDTOs_Then_ReturnsNull() {
    // When
    List<ProjectDTO> DTOs = projectMapper.mapToDTO(null);

    // Then
    Assertions.assertNull(DTOs);
  }

  @ParameterizedTest
  @MethodSource("provideProjectDTOAndProjectToUpdate")
  @Override
  public void Given_DTOAndEntity_When_UpdatingEntityByDTO_Then_PropertiesShouldBeEquals(
      ProjectDTO projectDTO, Project project) {
    // Given
    Project assertionProject = project.toBuilder().build();

    // When
    projectMapper.update(projectDTO, project);

    // Then
    Assertions.assertEquals(assertionProject.getId(), project.getId());
    Assertions.assertEquals(projectDTO.getName(), project.getName());
    Assertions.assertNotNull(assertionProject.getCreatedAt());
    Assertions.assertNotNull(project.getCreatedAt());
    Assertions.assertTrue(assertionProject.getCreatedAt().isEqual(project.getCreatedAt()));
    Assertions.assertNotNull(assertionProject.getModifiedAt());
    Assertions.assertNotNull(project.getModifiedAt());
    Assertions.assertTrue(assertionProject.getModifiedAt().isEqual(project.getModifiedAt()));
    Assertions.assertEquals(assertionProject.getVersion(), project.getVersion());
  }

  @ParameterizedTest
  @MethodSource("provideInvalidProjectDTOAndProjectToUpdate")
  @Override
  public void Given_DTOAndEntity_When_UpdatingEntityByInvalidDTO_Then_PropertiesShouldNotBeEquals(
      ProjectDTO projectDTO, Project project) {
    // Given
    Project assertionProject = project.toBuilder().build();

    // When
    projectMapper.update(projectDTO, project);

    // Then
    Assertions.assertEquals(assertionProject.getId(), project.getId());
    Assertions.assertEquals(assertionProject.getName(), project.getName());
    Assertions.assertNotNull(assertionProject.getCreatedAt());
    Assertions.assertNotNull(project.getCreatedAt());
    Assertions.assertTrue(assertionProject.getCreatedAt().isEqual(project.getCreatedAt()));
    Assertions.assertNotNull(assertionProject.getModifiedAt());
    Assertions.assertNotNull(project.getModifiedAt());
    Assertions.assertTrue(assertionProject.getModifiedAt().isEqual(project.getModifiedAt()));
    Assertions.assertEquals(assertionProject.getVersion(), project.getVersion());
  }
}
