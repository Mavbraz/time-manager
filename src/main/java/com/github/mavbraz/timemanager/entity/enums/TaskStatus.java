package com.github.mavbraz.timemanager.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum TaskStatus {
  NOT_STARTED("NOT_STARTED"),
  STARTED("STARTED"),
  FINISHED("FINISHED");

  @JsonValue
  private final String description;

  TaskStatus(String description) {
    this.description = description;
  }

  public static TaskStatus from(String description) {
    // Used simple for-loop instead Stream, because of performance
    for (TaskStatus status : values()) {
      if (status.getDescription().equals(description)) {
        return status;
      }
    }

    return NOT_STARTED;
  }

  @Override
  public String toString() {
    return getDescription();
  }
}
