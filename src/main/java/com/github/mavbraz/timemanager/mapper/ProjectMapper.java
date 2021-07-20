package com.github.mavbraz.timemanager.mapper;

import com.github.mavbraz.timemanager.dto.ProjectDTO;
import com.github.mavbraz.timemanager.entity.Project;
import org.mapstruct.Mapper;

/**
 * Mapper for convert "Project" and "ProjectDTO".
 *
 * @see Mapper
 */
@Mapper(config = BaseConfig.class)
public interface ProjectMapper extends BaseMapper<Project, ProjectDTO> {}
