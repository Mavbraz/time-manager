package com.github.mavbraz.timemanager.unit_tests.mapper;

import com.github.mavbraz.timemanager.dto.PersonDTO;
import com.github.mavbraz.timemanager.entity.Person;
import com.github.mavbraz.timemanager.helper.ParameterizedTestHelper;
import com.github.mavbraz.timemanager.mapper.PersonMapper;
import com.github.mavbraz.timemanager.mapper.PersonMapperImpl;
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
@ContextConfiguration(classes = {PersonMapperImpl.class})
public class PersonMapperTest implements MapperTest<Person, PersonDTO> {

  @Autowired private PersonMapper personMapper;

  private static Stream<Arguments> provideValidPersonDTO() {
    return Stream.of(
        Arguments.of(
            PersonDTO.builder()
                .id("DTO 1")
                .name("DTO Name 1")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build()));
  }

  private static Stream<Arguments> provideInvalidPersonDTO() {
    return Stream.of(
        Arguments.of(
            PersonDTO.builder().id(null).name(null).createdAt(null).modifiedAt(null).build()),
        Arguments.of(
            PersonDTO.builder().id("  ").name("  ").createdAt(null).modifiedAt(null).build()));
  }

  private static Stream<Arguments> provideValidPerson() {
    return Stream.of(
        Arguments.of(
            Person.builder()
                .id("Entity 2")
                .name("Entity Name 2")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .version(1)
                .build()));
  }

  private static Stream<Arguments> providePeople() {
    return ParameterizedTestHelper.join(provideValidPerson());
  }

  private static Stream<Arguments> providePersonDTOAndPersonToUpdate() {
    return ParameterizedTestHelper.cartesian(provideValidPersonDTO(), provideValidPerson());
  }

  @ParameterizedTest
  @MethodSource({"provideValidPersonDTO", "provideInvalidPersonDTO"})
  @Override
  public void Given_DTO_When_ConvertingToEntity_Then_PropertiesShouldBeEquals(PersonDTO personDTO) {
    // When
    Person person = personMapper.toEntity(personDTO);

    // Then
    Assertions.assertNotNull(person);
    Assertions.assertNull(person.getId());
    Assertions.assertEquals(personDTO.getName(), person.getName());
    Assertions.assertNull(person.getCreatedAt());
    Assertions.assertNull(person.getModifiedAt());
    Assertions.assertEquals(0L, person.getVersion());
  }

  @Test
  @Override
  public void Given_DTO_When_ConvertingNullToEntity_Then_ReturnsNull() {
    // When
    Person person = personMapper.toEntity(null);

    // Then
    Assertions.assertNull(person);
  }

  @ParameterizedTest
  @MethodSource("provideValidPerson")
  @Override
  public void Given_Entity_When_ConvertingToDTO_Then_PropertiesShouldBeEquals(Person person) {
    // When
    PersonDTO personDTO = personMapper.toDTO(person);

    // Then
    Assertions.assertNotNull(personDTO);
    Assertions.assertEquals(person.getId(), personDTO.getId());
    Assertions.assertEquals(person.getName(), personDTO.getName());
    Assertions.assertNotNull(person.getCreatedAt());
    Assertions.assertNotNull(personDTO.getCreatedAt());
    Assertions.assertTrue(person.getCreatedAt().isEqual(personDTO.getCreatedAt()));
    Assertions.assertNotNull(person.getModifiedAt());
    Assertions.assertNotNull(personDTO.getModifiedAt());
    Assertions.assertTrue(person.getModifiedAt().isEqual(personDTO.getModifiedAt()));
  }

  @Test
  @Override
  public void Given_Entity_When_ConvertingNullToDTO_Then_ReturnsNull() {
    // When
    PersonDTO personDTO = personMapper.toDTO(null);

    // Then
    Assertions.assertNull(personDTO);
  }

  @ParameterizedTest
  @MethodSource("providePeople")
  @Override
  public void Given_Entities_When_ConvertingToDTOs_Then_PropertiesShouldBeEquals(
      List<Person> people) {
    // When
    List<PersonDTO> DTOs = personMapper.mapToDTO(people);

    // Then
    Assertions.assertNotNull(DTOs);
    Assertions.assertEquals(people.size(), DTOs.size());

    for (int i = 0; i < DTOs.size(); i++) {
      Person person = people.get(i);
      PersonDTO personDTO = DTOs.get(i);

      Assertions.assertEquals(person.getId(), personDTO.getId());
      Assertions.assertEquals(person.getName(), personDTO.getName());
      Assertions.assertNotNull(person.getCreatedAt());
      Assertions.assertNotNull(personDTO.getCreatedAt());
      Assertions.assertTrue(person.getCreatedAt().isEqual(personDTO.getCreatedAt()));
      Assertions.assertNotNull(person.getModifiedAt());
      Assertions.assertNotNull(personDTO.getModifiedAt());
      Assertions.assertTrue(person.getModifiedAt().isEqual(personDTO.getModifiedAt()));
    }
  }

  @Test
  @Override
  public void Given_Entities_When_ConvertingNullToDTOs_Then_ReturnsNull() {
    // When
    List<PersonDTO> DTOs = personMapper.mapToDTO(null);

    // Then
    Assertions.assertNull(DTOs);
  }

  @ParameterizedTest
  @MethodSource("providePersonDTOAndPersonToUpdate")
  @Override
  public void Given_DTOAndEntity_When_UpdatingEntityByDTO_Then_PropertiesShouldBeEquals(
      PersonDTO personDTO, Person person) {
    // Given
    Person assertionPerson = person.toBuilder().build();

    // When
    personMapper.update(personDTO, person);

    // Then
    Assertions.assertEquals(assertionPerson.getId(), person.getId());
    Assertions.assertEquals(personDTO.getName(), person.getName());
    Assertions.assertNotNull(assertionPerson.getCreatedAt());
    Assertions.assertNotNull(person.getCreatedAt());
    Assertions.assertTrue(assertionPerson.getCreatedAt().isEqual(person.getCreatedAt()));
    Assertions.assertNotNull(assertionPerson.getModifiedAt());
    Assertions.assertNotNull(person.getModifiedAt());
    Assertions.assertTrue(assertionPerson.getModifiedAt().isEqual(person.getModifiedAt()));
    Assertions.assertEquals(assertionPerson.getVersion(), person.getVersion());
  }

  @ParameterizedTest
  @MethodSource("provideValidPerson")
  @Override
  public void Given_DTOAndEntity_When_UpdatingEntityByInvalidDTO_Then_PropertiesShouldNotBeEquals(
      PersonDTO personDTO, Person person) {
    // Given
    Person assertionPerson = person.toBuilder().build();

    // When
    personMapper.update(null, person);

    // Then
    Assertions.assertEquals(assertionPerson.getId(), person.getId());
    Assertions.assertEquals(assertionPerson.getName(), person.getName());
    Assertions.assertNotNull(assertionPerson.getCreatedAt());
    Assertions.assertNotNull(person.getCreatedAt());
    Assertions.assertTrue(assertionPerson.getCreatedAt().isEqual(person.getCreatedAt()));
    Assertions.assertNotNull(assertionPerson.getModifiedAt());
    Assertions.assertNotNull(person.getModifiedAt());
    Assertions.assertTrue(assertionPerson.getModifiedAt().isEqual(person.getModifiedAt()));
    Assertions.assertEquals(assertionPerson.getVersion(), person.getVersion());
  }
}
