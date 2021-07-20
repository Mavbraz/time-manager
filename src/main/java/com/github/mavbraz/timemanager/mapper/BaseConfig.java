package com.github.mavbraz.timemanager.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Base used to share mapper configurations.
 *
 * <p>Configurations:
 *
 * <ul>
 *   <li>Null value properties are ignored
 * </ul>
 *
 * @see MapperConfig
 */
@MapperConfig(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BaseConfig {}
