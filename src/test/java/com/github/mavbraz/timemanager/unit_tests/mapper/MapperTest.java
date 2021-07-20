package com.github.mavbraz.timemanager.unit_tests.mapper;

import com.github.mavbraz.timemanager.dto.BaseDTO;
import com.github.mavbraz.timemanager.entity.BaseDocument;
import java.util.List;

public interface MapperTest<E extends BaseDocument, D extends BaseDTO> {

  void Given_DTO_When_ConvertingToEntity_Then_PropertiesShouldBeEquals(D dto);

  void Given_DTO_When_ConvertingNullToEntity_Then_ReturnsNull();

  void Given_Entity_When_ConvertingToDTO_Then_PropertiesShouldBeEquals(E entity);

  void Given_Entity_When_ConvertingNullToDTO_Then_ReturnsNull();

  void Given_Entities_When_ConvertingToDTOs_Then_PropertiesShouldBeEquals(List<E> entities);

  void Given_Entities_When_ConvertingNullToDTOs_Then_ReturnsNull();

  void Given_DTOAndEntity_When_UpdatingEntityByDTO_Then_PropertiesShouldBeEquals(D dto, E entity);

  void Given_DTOAndEntity_When_UpdatingEntityByInvalidDTO_Then_PropertiesShouldNotBeEquals(
      D dto, E entity);
}
