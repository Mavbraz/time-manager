package com.github.mavbraz.timemanager.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum TaskStatusDTO {
  NOT_STARTED("NOT_STARTED"),
  STARTED("STARTED"),
  FINISHED("FINISHED");

  @JsonValue
  private final String description;

  TaskStatusDTO(String description) {
    this.description = description;
  }

  public static TaskStatusDTO from(String description) {
    // Used simple for-loop instead Stream, because of performance
    for (TaskStatusDTO status : values()) {
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
