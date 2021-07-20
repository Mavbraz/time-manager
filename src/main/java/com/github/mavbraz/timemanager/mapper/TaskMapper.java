package com.github.mavbraz.timemanager.mapper;

import com.github.mavbraz.timemanager.dto.TaskDTO;
import com.github.mavbraz.timemanager.entity.Task;
import com.github.mavbraz.timemanager.mapper.annotations.InitialTask;
import com.github.mavbraz.timemanager.mapper.annotations.WithoutDefaultProperties;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper for convert "Task" and "TaskDTO".
 *
 * @see Mapper
 */
@Mapper(config = BaseConfig.class)
public interface TaskMapper extends BaseMapper<Task, TaskDTO> {

  @InitialTask
  @WithoutDefaultProperties
  @Override
  Task toEntity(TaskDTO dto);

  @InitialTask
  @WithoutDefaultProperties
  @Override
  void update(TaskDTO dto, @MappingTarget Task entity);
}
