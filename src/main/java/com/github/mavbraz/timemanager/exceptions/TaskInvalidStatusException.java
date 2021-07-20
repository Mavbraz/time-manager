package com.github.mavbraz.timemanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TaskInvalidStatusException extends ResponseStatusException {

  private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

  public TaskInvalidStatusException() {
    super(HTTP_STATUS);
  }

  public TaskInvalidStatusException(String reason) {
    super(HTTP_STATUS, reason);
  }

  public TaskInvalidStatusException(String reason, Throwable cause) {
    super(HTTP_STATUS, reason, cause);
  }
}
