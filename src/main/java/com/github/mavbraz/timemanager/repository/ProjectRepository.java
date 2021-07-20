package com.github.mavbraz.timemanager.repository;

import com.github.mavbraz.timemanager.entity.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for operate the entity "Project" in MongoDB.
 *
 * @see MongoRepository
 */
public interface ProjectRepository extends MongoRepository<Project, String> {}
