package com.github.mavbraz.timemanager.controller;

import com.github.mavbraz.timemanager.dto.ProjectDTO;
import com.github.mavbraz.timemanager.entity.Project;
import com.github.mavbraz.timemanager.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller for path "/project".
 *
 * @see RestController
 * @see RequestMapping
 */
@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController<Project, ProjectDTO, ProjectMapper> {

  /**
   * Instantiates with the services autowired by Spring.
   *
   * @param repository the repository service
   * @param projectMapper the mapper service
   */
  @Autowired
  public ProjectController(
      MongoRepository<Project, String> repository, ProjectMapper projectMapper) {
    super(repository, projectMapper);
  }
}
