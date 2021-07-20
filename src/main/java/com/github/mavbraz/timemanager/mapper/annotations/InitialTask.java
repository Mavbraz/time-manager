package com.github.mavbraz.timemanager.mapper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.mapstruct.Mapping;

/**
 * Used for ignore some fields from DTO.
 *
 * <p><i>Based on <a
 * href="https://stackoverflow.com/questions/64423822/mapstruct-ignore-field-by-name-in-whole-project">Stack
 * Overflow Solution</a></i>
 *
 * @see Mapping
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
@Mapping(target = "startDate", ignore = true)
@Mapping(target = "finishDate", ignore = true)
@Mapping(target = "status", ignore = true)
public @interface InitialTask {}
