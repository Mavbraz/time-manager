package com.github.mavbraz.timemanager.integration_tests.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.mavbraz.timemanager.dto.PersonDTO;
import com.github.mavbraz.timemanager.entity.Person;
import com.github.mavbraz.timemanager.exceptions.ResourceNotFoundException;
import com.github.mavbraz.timemanager.helper.JsonTestHelper;
import com.github.mavbraz.timemanager.helper.matcher.DataTimeMatcher;
import com.github.mavbraz.timemanager.repository.PersonRepository;
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
public class PersonControllerTest extends BaseIntegrationTest<Person, PersonDTO> {

  @Autowired private MockMvc mvc;
  @Autowired private PersonRepository personRepository;

  private static Stream<Arguments> provideValidPersonDTO() {
    return Stream.of(
        Arguments.of(PersonDTO.builder().name("Person DTO Name").build()),
        Arguments.of(PersonDTO.builder().name("   a ").build()));
  }

  private static Stream<Arguments> provideInvalidPersonDTO() {
    return Stream.of(
        Arguments.of(PersonDTO.builder().name(null).build()),
        Arguments.of(PersonDTO.builder().name("     ").build()));
  }

  private static Stream<Arguments> provideValidPerson() {
    return Stream.of(
        Arguments.of(Person.builder().name("Person DTO Name").build()),
        Arguments.of(Person.builder().name("   a ").build()));
  }

  private static Stream<Arguments> provideInvalidId() {
    return Stream.of(
        Arguments.of("null"), Arguments.of("   "), Arguments.of("Person ID Not Found"));
  }

  private static Stream<Arguments> provideValidPeople() {
    return Stream.of(
        Arguments.of(
            List.of(
                Person.builder().name("Person DTO Name").build(),
                Person.builder().name("   a ").build())),
        Arguments.of(List.of()));
  }

  @ParameterizedTest
  @MethodSource("provideValidPersonDTO")
  @Override
  public void Given_Entity_When_Creating_Then_Return302(PersonDTO personDTO) throws Exception {
    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonTestHelper.asJsonString(personDTO)));

    // Then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.not(personDTO.getId())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt", DataTimeMatcher.valid()))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.createdAt", DataTimeMatcher.before(LocalDateTime.now())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedAt", DataTimeMatcher.valid()))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.modifiedAt", DataTimeMatcher.before(LocalDateTime.now())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(personDTO.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedAt", DataTimeMatcher.valid()))
        .andExpect(
            result -> {
              MockHttpServletResponse response = result.getResponse();
              Assertions.assertNotNull(response);

              String json = response.getContentAsString();
              PersonDTO personDTOSaved = JsonTestHelper.fromJsonString(json, PersonDTO.class);
              Assertions.assertNotNull(personDTO);

              Person personSaved = personRepository.findById(personDTOSaved.getId()).orElse(null);
              Assertions.assertNotNull(personSaved);
              Assertions.assertEquals(personSaved.getId(), personDTOSaved.getId());
              Assertions.assertEquals(personSaved.getName(), personDTOSaved.getName());
              Assertions.assertNotNull(personSaved.getCreatedAt());
              Assertions.assertNotNull(personDTOSaved.getCreatedAt());
              Assertions.assertTrue(
                  personSaved.getCreatedAt().isEqual(personDTOSaved.getCreatedAt()));
              Assertions.assertNotNull(personSaved.getModifiedAt());
              Assertions.assertNotNull(personDTOSaved.getModifiedAt());
              Assertions.assertTrue(
                  personSaved.getModifiedAt().isEqual(personDTOSaved.getModifiedAt()));
              Assertions.assertEquals(1, personSaved.getVersion());
            });
  }

  @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
  @ParameterizedTest
  @MethodSource("provideInvalidPersonDTO")
  @Override
  public void Given_Entity_When_CreatingInvalid_Then_Return400(PersonDTO personDTO)
      throws Exception {
    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonTestHelper.asJsonString(personDTO)));

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
              Assertions.assertEquals("personDTO", fieldError.getObjectName());
              Assertions.assertEquals("name", fieldError.getField());
              Assertions.assertEquals(personDTO.getName(), fieldError.getRejectedValue());
              Assertions.assertEquals("NotBlank", fieldError.getCode());
            });
  }

  @ParameterizedTest
  @MethodSource("provideValidPeople")
  @Override
  public void Given_Entity_When_GettingAll_Then_Return404(List<Person> people) throws Exception {
    // Given
    List<Person> peopleSaved = personRepository.saveAll(people);

    // When
    ResultActions resultActions =
        mvc.perform(MockMvcRequestBuilders.get("/person").contentType(MediaType.APPLICATION_JSON));

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
              List<PersonDTO> peopleDTO =
                  JsonTestHelper.fromJsonString(json, new TypeReference<>() {});
              Assertions.assertNotNull(peopleDTO);

              Assertions.assertEquals(peopleSaved.size(), peopleDTO.size());
              for (int i = 0; i < peopleDTO.size(); i++) {
                Person person = peopleSaved.get(i);
                PersonDTO personDTO = peopleDTO.get(i);

                Assertions.assertEquals(person.getId(), personDTO.getId());
                Assertions.assertEquals(person.getName(), personDTO.getName());
                MatcherAssert.assertThat(
                    DataTimeMatcher.toString(personDTO.getCreatedAt()),
                    DataTimeMatcher.is(person.getCreatedAt()));
                MatcherAssert.assertThat(
                    DataTimeMatcher.toString(personDTO.getModifiedAt()),
                    DataTimeMatcher.is(person.getModifiedAt()));
              }
            });
  }

  @ParameterizedTest
  @MethodSource("provideValidPerson")
  @Override
  public void Given_Entity_When_Getting_Then_Return200(Person person) throws Exception {
    // Given
    Person personSaved = personRepository.save(person);

    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.get("/person/{id}", personSaved.getId())
                .contentType(MediaType.APPLICATION_JSON));

    // Then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(personSaved.getId())))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.createdAt", DataTimeMatcher.is(personSaved.getCreatedAt())))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.modifiedAt", DataTimeMatcher.is(personSaved.getModifiedAt())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(personSaved.getName())));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidId")
  @Override
  public void Given_Entity_When_GettingNonExisting_Then_Return404(String id) throws Exception {
    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.get("/person/{id}", id).contentType(MediaType.APPLICATION_JSON));

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
              Assertions.assertEquals("404 NOT_FOUND \"Person not found\"", exception.getMessage());
              Assertions.assertNotNull(exception.getReason());
              Assertions.assertEquals("Person not found", exception.getReason());
            });
  }

  @ParameterizedTest
  @MethodSource("provideValidPersonDTO")
  @Override
  public void Given_Entity_When_Updating_Then_Return200(PersonDTO personDTO)
      throws Exception {
    // Given
    Person personSaved = personRepository.save(Person.builder().name("Person DTO 1").build());

    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.put("/person/{id}", personSaved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonTestHelper.asJsonString(personDTO)));

    // Then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(personSaved.getId())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt", DataTimeMatcher.valid()))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.createdAt", DataTimeMatcher.is(personSaved.getCreatedAt())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedAt", DataTimeMatcher.valid()))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.modifiedAt", DataTimeMatcher.before(LocalDateTime.now())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(personDTO.getName())))
        .andExpect(
            result -> {
              MockHttpServletResponse response = result.getResponse();
              Assertions.assertNotNull(response);

              String json = response.getContentAsString();
              PersonDTO personDTOSaved = JsonTestHelper.fromJsonString(json, PersonDTO.class);
              Assertions.assertNotNull(personDTO);

              Person personUpdated = personRepository.findById(personDTOSaved.getId()).orElse(null);
              Assertions.assertNotNull(personUpdated);
              Assertions.assertEquals(personUpdated.getId(), personDTOSaved.getId());
              Assertions.assertEquals(personUpdated.getName(), personDTOSaved.getName());
              Assertions.assertNotNull(personUpdated.getCreatedAt());
              Assertions.assertNotNull(personDTOSaved.getCreatedAt());
              Assertions.assertTrue(
                  personUpdated.getCreatedAt().isEqual(personDTOSaved.getCreatedAt()));
              Assertions.assertNotNull(personUpdated.getModifiedAt());
              Assertions.assertNotNull(personDTOSaved.getModifiedAt());
              Assertions.assertTrue(
                  personUpdated.getModifiedAt().isEqual(personDTOSaved.getModifiedAt()));
              Assertions.assertEquals(2, personUpdated.getVersion());
            });
  }

  @ParameterizedTest
  @MethodSource("provideInvalidPersonDTO")
  @Override
  public void Given_Entity_When_UpdatingInvalid_Then_Return400(PersonDTO personDTO)
      throws Exception {
    // Given
    Person personSaved = personRepository.save(Person.builder().name("Person DTO 1").build());

    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.put("/person/{id}", personSaved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonTestHelper.asJsonString(personDTO)));

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
              Assertions.assertEquals("personDTO", fieldError.getObjectName());
              Assertions.assertEquals("name", fieldError.getField());
              Assertions.assertEquals(personDTO.getName(), fieldError.getRejectedValue());
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
            MockMvcRequestBuilders.put("/person/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    JsonTestHelper.asJsonString(PersonDTO.builder().name("Person DTO 1").build())));

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
              Assertions.assertEquals("404 NOT_FOUND \"Person not found\"", exception.getMessage());
              Assertions.assertNotNull(exception.getReason());
              Assertions.assertEquals("Person not found", exception.getReason());
            });
  }

  @Test
  @Override
  public void Given_Entity_When_Removing_Then_Return200() throws Exception {
    // Given
    Person personSaved = personRepository.save(Person.builder().name("Person DTO 1").build());

    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.delete("/person/{id}", personSaved.getId())
                .contentType(MediaType.APPLICATION_JSON));

    // Then
    Assertions.assertNotNull(personSaved.getId());
    resultActions
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(personSaved.getId())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt", DataTimeMatcher.valid()))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.createdAt", DataTimeMatcher.is(personSaved.getCreatedAt())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedAt", DataTimeMatcher.valid()))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.modifiedAt", DataTimeMatcher.is(personSaved.getModifiedAt())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(personSaved.getName())));
    Assertions.assertFalse(personRepository.existsById(personSaved.getId()));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidId")
  @Override
  public void Given_Entity_When_RemovingNonExisting_Then_Return404(String id) throws Exception {
    // When
    ResultActions resultActions =
        mvc.perform(
            MockMvcRequestBuilders.delete("/person/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    JsonTestHelper.asJsonString(PersonDTO.builder().name("Person DTO 1").build())));

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
              Assertions.assertEquals("404 NOT_FOUND \"Person not found\"", exception.getMessage());
              Assertions.assertNotNull(exception.getReason());
              Assertions.assertEquals("Person not found", exception.getReason());
            });
  }
}
