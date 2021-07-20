package com.github.mavbraz.timemanager.controller;

import com.github.mavbraz.timemanager.dto.PersonDTO;
import com.github.mavbraz.timemanager.entity.Person;
import com.github.mavbraz.timemanager.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller for path "/person".
 *
 * @see RestController
 * @see RequestMapping
 */
@RestController
@RequestMapping("/person")
public class PersonController extends BaseController<Person, PersonDTO, PersonMapper> {

  /**
   * Instantiates with the services autowired by Spring.
   *
   * @param repository the repository service
   * @param personMapper the mapper service
   */
  @Autowired
  public PersonController(MongoRepository<Person, String> repository, PersonMapper personMapper) {
    super(repository, personMapper);
  }
}
