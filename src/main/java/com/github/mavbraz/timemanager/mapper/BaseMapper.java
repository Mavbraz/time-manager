package com.github.mavbraz.timemanager.mapper;

import com.github.mavbraz.timemanager.dto.BaseDTO;
import com.github.mavbraz.timemanager.entity.BaseDocument;
import com.github.mavbraz.timemanager.mapper.annotations.WithoutDefaultProperties;
import java.util.List;
import org.mapstruct.MappingTarget;

/**
 * Base mapper for conversion.
 *
 * @param <E> the entity
 * @param <D> the DTO
 */
public interface BaseMapper<E extends BaseDocument, D extends BaseDTO> {

  /**
   * Converts DTO to entity.
   *
   * @param dto the DTO
   * @return the entity
   */
  @WithoutDefaultProperties
  E toEntity(D dto);

  /**
   * Converts entity to DTO.
   *
   * @param entity the entity
   * @return the DTO
   */
  D toDTO(E entity);

  /**
   * Convert all entities to DTOs.
   *
   * @param entities the entities
   * @return the DTOs
   */
  List<D> mapToDTO(List<E> entities);

  /**
   * Updates entity through DTO.
   *
   * @param dto the DTO
   * @param entity the entity
   */
  @WithoutDefaultProperties
  void update(D dto, @MappingTarget E entity);
}
