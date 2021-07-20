package com.github.mavbraz.timemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Application startup class.
 *
 * @see SpringBootApplication
 * @see EnableMongoAuditing
 */
@SpringBootApplication
@EnableMongoAuditing
public class TimeManagerApplication {

  /**
   * The entry point of application. Running SpringApplication.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(TimeManagerApplication.class, args);
  }
}
