package com.github.mavbraz.timemanager.integration_tests.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.mavbraz.timemanager.dto.ProjectDTO;
import com.github.mavbraz.timemanager.entity.Project;
import com.github.mavbraz.timemanager.exceptions.ResourceNotFoundException;
import com.github.mavbraz.timemanager.helper.JsonTestHelper;
import com.github.mavbraz.timemanager.helper.matcher.DataTimeMatcher;
import com.github.mavbraz.timemanager.repository.ProjectRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class ProjectControllerTest extends BaseIntegrationTest<Project, ProjectDTO> {

  @Autowired private MockMvc mvc;
  @Autowired private ProjectRepository projectRepository;

  private static Stream<Arguments> provideValidProjectDTO() {
    return Stream.of(
        Arguments.of(ProjectDTO.builder().name("Project DTO Name").build()),
        Arguments.of(ProjectDTO.builder().name("   a ").build()));
  }

  private static Stream<Arguments> provideInvalidProjectDTO() {
    return Stream.of(
        Arguments.of(ProjectDTO.builder().name(null).build()),
        Arguments.of(ProjectDTO.builder().name("     ").build()));
  }

  private static Stream<Arguments> provideValidProject() {
    return Stream.of(
        Arguments.of(Project.builder().name("Project DTO Name").build()),
        Arguments.of(Project.builder().name("   a ").build()));
  }

  private static Stream<Arguments> provideInvalidId() {
    return Stream.of(
        Arguments.of("null"), Arguments.of("   "), Arguments.of("Project ID Not Found"));
  }

  private static Stream<Arguments> provideValidProjects() {
    return Stream.of(
        Arguments.of(
            List.of(
                Project.builder().name("Project DTO Name").build(),
                Project.builder().name("   a ").build())),
        Arguments.of(List.of()));
  }

  @ParameterizedTest
  @MethodSource("provideValidProjectDTO")
  @Override
  public void Given_Entity_When_Creating_Then_Return302(ProjectDTO projectDTO) throws Exception {
    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.post("/project")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonTestHelper.asJsonString(projectDTO)));

    // Then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.not(projectDTO.getId())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt", DataTimeMatcher.valid()))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.createdAt", DataTimeMatcher.before(LocalDateTime.now())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedAt", DataTimeMatcher.valid()))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.modifiedAt", DataTimeMatcher.before(LocalDateTime.now())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(projectDTO.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedAt", DataTimeMatcher.valid()))
        .andExpect(
            result -> {
              MockHttpServletResponse response = result.getResponse();
              Assertions.assertNotNull(response);

              String json = response.getContentAsString();
              ProjectDTO projectDTOSaved = JsonTestHelper.fromJsonString(json, ProjectDTO.class);
              Assertions.assertNotNull(projectDTO);

              Project projectSaved =
                  projectRepository.findById(projectDTOSaved.getId()).orElse(null);
              Assertions.assertNotNull(projectSaved);
              Assertions.assertEquals(projectSaved.getId(), projectDTOSaved.getId());
              Assertions.assertEquals(projectSaved.getName(), projectDTOSaved.getName());
              Assertions.assertNotNull(projectSaved.getCreatedAt());
              Assertions.assertNotNull(projectDTOSaved.getCreatedAt());
              Assertions.assertTrue(
                  projectSaved.getCreatedAt().isEqual(projectDTOSaved.getCreatedAt()));
              Assertions.assertNotNull(projectSaved.getModifiedAt());
              Assertions.assertNotNull(projectDTOSaved.getModifiedAt());
              Assertions.assertTrue(
                  projectSaved.getModifiedAt().isEqual(projectDTOSaved.getModifiedAt()));
              Assertions.assertEquals(1, projectSaved.getVersion());
            });
  }

  @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
  @ParameterizedTest
  @MethodSource("provideInvalidProjectDTO")
  @Override
  public void Given_Entity_When_CreatingInvalid_Then_Return400(ProjectDTO projectDTO)
      throws Exception {
    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.post("/project")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonTestHelper.asJsonString(projectDTO)));

    // Then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(
            result -> {
              Assertions.assertTrue(
                  result.getResolvedException() instanceof MethodArgumentNotValidException);
              MethodArgumentNotValidException exception =
                  (MethodArgumentNotValidException) result.getResolvedException();
              List<ObjectError> allErrors = exception.getAllErrors();

              Assertions.assertNotNull(exception.getMessage());
              Assertions.assertEquals(1, allErrors.size());

              Assertions.assertTrue(allErrors.get(0) instanceof FieldError);
              FieldError fieldError = (FieldError) allErrors.get(0);
              Assertions.assertEquals("must not be blank", fieldError.getDefaultMessage());
              Assertions.assertEquals("projectDTO", fieldError.getObjectName());
              Assertions.assertEquals("name", fieldError.getField());
              Assertions.assertEquals(projectDTO.getName(), fieldError.getRejectedValue());
              Assertions.assertEquals("NotBlank", fieldError.getCode());
            });
  }

  @ParameterizedTest
  @MethodSource("provideValidProjects")
  @Override
  public void Given_Entity_When_GettingAll_Then_Return404(List<Project> projects)
      throws Exception {
    // Given
    List<Project> projectsSaved = projectRepository.saveAll(projects);

    // When
    ResultActions resultActions =
        mvc.perform(MockMvcRequestBuilders.get("/project").contentType(MediaType.APPLICATION_JSON));

    // Then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(
            result -> {
              MockHttpServletResponse response = result.getResponse();
              Assertions.assertNotNull(response);

              String json = response.getContentAsString();
              List<ProjectDTO> projectsDTO =
                  JsonTestHelper.fromJsonString(json, new TypeReference<>() {});
              Assertions.assertNotNull(projectsDTO);

              Assertions.assertEquals(projectsSaved.size(), projectsDTO.size());
              for (int i = 0; i < projectsDTO.size(); i++) {
                Project project = projectsSaved.get(i);
                ProjectDTO projectDTO = projectsDTO.get(i);

                Assertions.assertEquals(project.getId(), projectDTO.getId());
                Assertions.assertEquals(project.getName(), projectDTO.getName());
                MatcherAssert.assertThat(
                    DataTimeMatcher.toString(projectDTO.getCreatedAt()),
                    DataTimeMatcher.is(project.getCreatedAt()));
                MatcherAssert.assertThat(
                    DataTimeMatcher.toString(projectDTO.getModifiedAt()),
                    DataTimeMatcher.is(project.getModifiedAt()));
              }
            });
  }

  @ParameterizedTest
  @MethodSource("provideValidProject")
  @Override
  public void Given_Entity_When_Getting_Then_Return200(Project project) throws Exception {
    // Given
    Project projectSaved = projectRepository.save(project);

    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.get("/project/{id}", projectSaved.getId())
                .contentType(MediaType.APPLICATION_JSON));

    // Then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(projectSaved.getId())))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.createdAt", DataTimeMatcher.is(projectSaved.getCreatedAt())))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.modifiedAt", DataTimeMatcher.is(projectSaved.getModifiedAt())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(projectSaved.getName())));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidId")
  @Override
  public void Given_Entity_When_GettingNonExisting_Then_Return404(String id) throws Exception {
    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.get("/project/{id}", id)
                .contentType(MediaType.APPLICATION_JSON));

    // Then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(
            result -> {
              Assertions.assertTrue(
                  result.getResolvedException() instanceof ResourceNotFoundException);
              ResourceNotFoundException exception =
                  (ResourceNotFoundException) result.getResolvedException();
              Assertions.assertNotNull(exception.getStatus());
              Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
              Assertions.assertNotNull(exception.getMessage());
              Assertions.assertEquals(
                  "404 NOT_FOUND \"Project not found\"", exception.getMessage());
              Assertions.assertNotNull(exception.getReason());
              Assertions.assertEquals("Project not found", exception.getReason());
            });
  }

  @ParameterizedTest
  @MethodSource("provideValidProjectDTO")
  @Override
  public void Given_Entity_When_Updating_Then_Return200(ProjectDTO projectDTO)
      throws Exception {
    // Given
    Project projectSaved = projectRepository.save(Project.builder().name("Project DTO 1").build());

    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.put("/project/{id}", projectSaved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonTestHelper.asJsonString(projectDTO)));

    // Then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(projectSaved.getId())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt", DataTimeMatcher.valid()))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.createdAt", DataTimeMatcher.is(projectSaved.getCreatedAt())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedAt", DataTimeMatcher.valid()))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.modifiedAt", DataTimeMatcher.before(LocalDateTime.now())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(projectDTO.getName())))
        .andExpect(
            result -> {
              MockHttpServletResponse response = result.getResponse();
              Assertions.assertNotNull(response);

              String json = response.getContentAsString();
              ProjectDTO projectDTOSaved = JsonTestHelper.fromJsonString(json, ProjectDTO.class);
              Assertions.assertNotNull(projectDTO);

              Project projectUpdated =
                  projectRepository.findById(projectDTOSaved.getId()).orElse(null);
              Assertions.assertNotNull(projectUpdated);
              Assertions.assertEquals(projectUpdated.getId(), projectDTOSaved.getId());
              Assertions.assertEquals(projectUpdated.getName(), projectDTOSaved.getName());
              Assertions.assertNotNull(projectUpdated.getCreatedAt());
              Assertions.assertNotNull(projectDTOSaved.getCreatedAt());
              Assertions.assertTrue(
                  projectUpdated.getCreatedAt().isEqual(projectDTOSaved.getCreatedAt()));
              Assertions.assertNotNull(projectUpdated.getModifiedAt());
              Assertions.assertNotNull(projectDTOSaved.getModifiedAt());
              Assertions.assertTrue(
                  projectUpdated.getModifiedAt().isEqual(projectDTOSaved.getModifiedAt()));
              Assertions.assertEquals(2, projectUpdated.getVersion());
            });
  }

  @ParameterizedTest
  @MethodSource("provideInvalidProjectDTO")
  @Override
  public void Given_Entity_When_UpdatingInvalid_Then_Return400(ProjectDTO projectDTO)
      throws Exception {
    // Given
    Project projectSaved = projectRepository.save(Project.builder().name("Project DTO 1").build());

    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.put("/project/{id}", projectSaved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonTestHelper.asJsonString(projectDTO)));

    // Then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(
            result -> {
              Assertions.assertTrue(
                  result.getResolvedException() instanceof MethodArgumentNotValidException);
              MethodArgumentNotValidException exception =
                  (MethodArgumentNotValidException) result.getResolvedException();
              List<ObjectError> allErrors = exception.getAllErrors();

              Assertions.assertNotNull(exception.getMessage());
              Assertions.assertEquals(1, allErrors.size());

              Assertions.assertTrue(allErrors.get(0) instanceof FieldError);
              FieldError fieldError = (FieldError) allErrors.get(0);
              Assertions.assertEquals("must not be blank", fieldError.getDefaultMessage());
              Assertions.assertEquals("projectDTO", fieldError.getObjectName());
              Assertions.assertEquals("name", fieldError.getField());
              Assertions.assertEquals(projectDTO.getName(), fieldError.getRejectedValue());
              Assertions.assertEquals("NotBlank", fieldError.getCode());
            });
  }

  @ParameterizedTest
  @MethodSource("provideInvalidId")
  @Override
  public void Given_Entity_When_UpdatingNonExisting_Then_Return404(String id) throws Exception {
    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.put("/project/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    JsonTestHelper.asJsonString(
                        ProjectDTO.builder().name("Project DTO 1").build())));

    // Then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(
            result -> {
              Assertions.assertTrue(
                  result.getResolvedException() instanceof ResourceNotFoundException);
              ResourceNotFoundException exception =
                  (ResourceNotFoundException) result.getResolvedException();
              Assertions.assertNotNull(exception.getStatus());
              Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
              Assertions.assertNotNull(exception.getMessage());
              Assertions.assertEquals(
                  "404 NOT_FOUND \"Project not found\"", exception.getMessage());
              Assertions.assertNotNull(exception.getReason());
              Assertions.assertEquals("Project not found", exception.getReason());
            });
  }

  @Test
  @Override
  public void Given_Entity_When_Removing_Then_Return200() throws Exception {
    // Given
    Project projectSaved = projectRepository.save(Project.builder().name("Project DTO 1").build());

    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.delete("/project/{id}", projectSaved.getId())
                .contentType(MediaType.APPLICATION_JSON));

    // Then
    Assertions.assertNotNull(projectSaved.getId());
    resultActions
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(projectSaved.getId())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt", DataTimeMatcher.valid()))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.createdAt", DataTimeMatcher.is(projectSaved.getCreatedAt())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedAt", DataTimeMatcher.valid()))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.modifiedAt", DataTimeMatcher.is(projectSaved.getModifiedAt())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(projectSaved.getName())));
    Assertions.assertFalse(projectRepository.existsById(projectSaved.getId()));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidId")
  @Override
  public void Given_Entity_When_RemovingNonExisting_Then_Return404(String id) throws Exception {
    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.delete("/project/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    JsonTestHelper.asJsonString(
                        ProjectDTO.builder().name("Project DTO 1").build())));

    // Then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(
            result -> {
              Assertions.assertTrue(
                  result.getResolvedException() instanceof ResourceNotFoundException);
              ResourceNotFoundException exception =
                  (ResourceNotFoundException) result.getResolvedException();
              Assertions.assertNotNull(exception.getStatus());
              Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
              Assertions.assertNotNull(exception.getMessage());
              Assertions.assertEquals(
                  "404 NOT_FOUND \"Project not found\"", exception.getMessage());
              Assertions.assertNotNull(exception.getReason());
              Assertions.assertEquals("Project not found", exception.getReason());
            });
  }
}
