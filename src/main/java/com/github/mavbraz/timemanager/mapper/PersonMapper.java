package com.github.mavbraz.timemanager.mapper;

import com.github.mavbraz.timemanager.dto.PersonDTO;
import com.github.mavbraz.timemanager.entity.Person;
import org.mapstruct.Mapper;

/**
 * Mapper for convert "Person" and "PersonDTO".
 *
 * @see Mapper
 */
@Mapper(config = BaseConfig.class)
public interface PersonMapper extends BaseMapper<Person, PersonDTO> {}
