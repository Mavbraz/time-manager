package com.github.mavbraz.timemanager.controller;

import com.github.mavbraz.timemanager.dto.BaseDTO;
import com.github.mavbraz.timemanager.entity.BaseDocument;
import com.github.mavbraz.timemanager.exceptions.ResourceNotFoundException;
import com.github.mavbraz.timemanager.mapper.BaseMapper;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Base of REST Controllers.
 *
 * @param <E> entity
 * @param <D> DTO
 * @param <M> mapper
 */
@Slf4j
public abstract class BaseController<
    E extends BaseDocument, D extends BaseDTO, M extends BaseMapper<E, D>> {

  /** Service for database operation. */
  protected final MongoRepository<E, String> repository;

  /** Service to map entity and DTO. */
  protected final M mapper;

  private final Class<E> controllerClass;

  /**
   * Instantiates with the services.
   *
   * @param repository the repository service
   * @param mapper the mapper service
   */
  @SuppressWarnings("unchecked")
  protected BaseController(MongoRepository<E, String> repository, M mapper) {
    this.repository = repository;
    this.mapper = mapper;
    this.controllerClass =
        (Class<E>)
            ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  /**
   * Creates the entity by details into database.
   *
   * @param details the DTO with updates
   * @return the created details
   * @throws ResourceNotFoundException if the entity is not found
   * @see PostMapping
   * @see ResponseStatus
   * @see HttpStatus
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public D create(@RequestBody @Valid D details) throws ResourceNotFoundException {
    var entity = mapper.toEntity(details);
    entity = repository.save(entity);
    entity = getEntityById(entity.getId());

    //    log.info("Creating {} with details {}", );

    return mapper.toDTO(entity);
  }

  /**
   * Gets all entities from database.
   *
   * @return all saved entities
   */
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<D> getAll() {
    var entities = repository.findAll();

    return mapper.mapToDTO(entities);
  }

  /**
   * Gets the entity by id from database.
   *
   * @param id the entity id
   * @return the saved details
   * @throws ResourceNotFoundException if the entity is not found
   */
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public D get(@PathVariable String id) throws ResourceNotFoundException {
    var entity = getEntityById(id);

    return mapper.toDTO(entity);
  }

  /**
   * Updates the entity by id from database.
   *
   * @param id the entity id
   * @param details the DTO with updates
   * @return the updated entity
   * @throws ResourceNotFoundException if the entity is not found
   */
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public D update(@PathVariable String id, @RequestBody @Valid D details)
      throws ResourceNotFoundException {
    var entity = getEntityById(id);
    mapper.update(details, entity);
    entity = repository.save(entity);
    entity = getEntityById(entity.getId());

    return mapper.toDTO(entity);
  }

  /**
   * Removes the entity by id from database.
   *
   * @param id the entity id
   * @return the removed entity
   * @throws ResourceNotFoundException if the entity is not found
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public D remove(@PathVariable String id) throws ResourceNotFoundException {
    var entity = getEntityById(id);
    repository.delete(entity);

    return mapper.toDTO(entity);
  }

  /**
   * Gets the entity by id from database.
   *
   * @param id the entity id
   * @return the saved entity
   * @throws ResourceNotFoundException if the entity is not found
   */
  protected E getEntityById(String id) throws ResourceNotFoundException {
    String entityName = getGenericSimpleName();

    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(entityName + " not found"));
  }

  /**
   * Gets generic simple name of controller class.
   *
   * @return the generic simple name
   */
  protected String getGenericSimpleName() {
    return controllerClass.getSimpleName();
  }
}
