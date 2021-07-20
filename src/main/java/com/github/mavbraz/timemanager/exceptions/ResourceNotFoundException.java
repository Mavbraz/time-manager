package com.github.mavbraz.timemanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception used for describe a resource that cannot be found.
 *
 * @see RuntimeException
 */
public class ResourceNotFoundException extends ResponseStatusException {

  private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

  /** Instantiates a new Resource not found exception. */
  public ResourceNotFoundException() {
    super(HTTP_STATUS);
  }

  /**
   * Instantiates a new Resource not found exception.
   *
   * @param message the message
   */
  public ResourceNotFoundException(String message) {
    super(HTTP_STATUS, message);
  }

  /**
   * Instantiates a new Resource not found exception.
   *
   * @param message the message
   * @param cause the cause
   * @see Throwable
   */
  public ResourceNotFoundException(String message, Throwable cause) {
    super(HTTP_STATUS, message, cause);
  }
}
