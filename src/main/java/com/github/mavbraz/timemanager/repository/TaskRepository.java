package com.github.mavbraz.timemanager.repository;

import com.github.mavbraz.timemanager.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for operate the entity "Task" in MongoDB.
 *
 * @see MongoRepository
 */
public interface TaskRepository extends MongoRepository<Task, String> {}
