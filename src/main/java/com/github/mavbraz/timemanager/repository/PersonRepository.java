package com.github.mavbraz.timemanager.repository;

import com.github.mavbraz.timemanager.entity.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for operate the entity "Person" in MongoDB.
 *
 * @see MongoRepository
 */
public interface PersonRepository extends MongoRepository<Person, String> {}
