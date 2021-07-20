package com.github.mavbraz.timemanager.controller;

import com.github.mavbraz.timemanager.dto.TaskDTO;
import com.github.mavbraz.timemanager.entity.Task;
import com.github.mavbraz.timemanager.entity.enums.TaskStatus;
import com.github.mavbraz.timemanager.exceptions.ResourceNotFoundException;
import com.github.mavbraz.timemanager.exceptions.TaskInvalidStatusException;
import com.github.mavbraz.timemanager.mapper.TaskMapper;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller for path "/task".
 *
 * @see RestController
 * @see RequestMapping
 */
@RestController
@RequestMapping("/task")
public class TaskController extends BaseController<Task, TaskDTO, TaskMapper> {

  /**
   * Instantiates with the services autowired by Spring.
   *
   * @param repository the repository service
   * @param taskMapper the mapper service
   */
  @Autowired
  public TaskController(MongoRepository<Task, String> repository, TaskMapper taskMapper) {
    super(repository, taskMapper);
  }

  /**
   * Starts task.
   *
   * @param id the entity id
   * @return the saved details
   * @throws ResourceNotFoundException if the entity is not found
   * @throws TaskInvalidStatusException if status is not "NOT_STARTED"
   */
  @PostMapping("/{id}/start")
  @ResponseBody
  public TaskDTO startTask(@PathVariable String id)
      throws ResourceNotFoundException, TaskInvalidStatusException {
    var entity = getEntityById(id);

    if (entity.getStatus() != TaskStatus.NOT_STARTED) {
      throw new TaskInvalidStatusException("Task has already been started or finished!");
    }

    entity.setStatus(TaskStatus.STARTED);
    entity.setStartDate(LocalDateTime.now());
    entity = repository.save(entity);

    return mapper.toDTO(entity);
  }

  /**
   * Finishes task.
   *
   * @param id the entity id
   * @return the saved details
   * @throws ResourceNotFoundException if the entity is not found
   * @throws TaskInvalidStatusException if status is not "STARTED"
   */
  @PostMapping("/{id}/finish")
  @ResponseBody
  public TaskDTO finishTask(@PathVariable String id)
      throws ResourceNotFoundException, TaskInvalidStatusException {
    var entity = getEntityById(id);

    if (entity.getStatus() != TaskStatus.STARTED) {
      throw new TaskInvalidStatusException("Task must have status \"STARTED\"!");
    }

    entity.setStatus(TaskStatus.FINISHED);
    entity.setFinishDate(LocalDateTime.now());
    entity = repository.save(entity);

    return mapper.toDTO(entity);
  }
}
