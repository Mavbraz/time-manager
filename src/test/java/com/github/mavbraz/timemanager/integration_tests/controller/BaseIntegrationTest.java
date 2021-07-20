package com.github.mavbraz.timemanager.integration_tests.controller;

import com.github.mavbraz.timemanager.dto.BaseDTO;
import com.github.mavbraz.timemanager.entity.BaseDocument;
import com.github.mavbraz.timemanager.repository.PersonRepository;
import com.github.mavbraz.timemanager.repository.ProjectRepository;
import com.github.mavbraz.timemanager.repository.TaskRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseIntegrationTest<E extends BaseDocument, D extends BaseDTO> {

  @Autowired private PersonRepository personRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private TaskRepository taskRepository;

  public BaseIntegrationTest() {}

  @AfterEach
  public void reset() {
    personRepository.deleteAll();
    projectRepository.deleteAll();
    taskRepository.deleteAll();
  }

  abstract void Given_Entity_When_Creating_Then_Return302(D dto) throws Exception;

  abstract void Given_Entity_When_CreatingInvalid_Then_Return400(D dto) throws Exception;

  abstract void Given_Entity_When_GettingAll_Then_Return404(List<E> entity) throws Exception;

  abstract void Given_Entity_When_Getting_Then_Return200(E entity) throws Exception;

  abstract void Given_Entity_When_GettingNonExisting_Then_Return404(String id) throws Exception;

  abstract void Given_Entity_When_Updating_Then_Return200(D dto) throws Exception;

  abstract void Given_Entity_When_UpdatingInvalid_Then_Return400(D dto) throws Exception;

  abstract void Given_Entity_When_UpdatingNonExisting_Then_Return404(String id) throws Exception;

  abstract void Given_Entity_When_Removing_Then_Return200() throws Exception;

  abstract void Given_Entity_When_RemovingNonExisting_Then_Return404(String id) throws Exception;
}
