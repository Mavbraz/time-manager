package com.github.mavbraz.timemanager.helper.matcher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

public class DataTimeMatcher {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

  private DataTimeMatcher() {}

  public static LocalDateTime fromString(String dateTime) {
    if (dateTime == null || dateTime.isBlank()) {
      return null;
    }

    try {
      return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
    } catch (Exception exception) {
      return null;
    }
  }

  public static String toString(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }

    try {
      //      return mapper.writeValueAsString(localDateTime.toString());
      return localDateTime.format(DATE_TIME_FORMATTER);
    } catch (Exception exception) {
      return null;
    }
  }

  public static Matcher<String> is(LocalDateTime localDateTime) {
    return new TypeSafeMatcher<>() {
      @Override
      protected boolean matchesSafely(String actual) {
        String expected = DataTimeMatcher.toString(localDateTime);

        return expected != null && expected.equals(actual);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("LocalDateTime should be " + localDateTime.toString());
      }

      @Override
      protected void describeMismatchSafely(String actual, Description description) {
        description.appendText("was " + actual);
      }
    };
  }

  public static Matcher<String> not(LocalDateTime localDateTime) {
    return Matchers.not(is(localDateTime));
  }

  public static Matcher<String> valid() {
    return new TypeSafeMatcher<>() {
      @Override
      protected boolean matchesSafely(String dateTime) {
        LocalDateTime dateTimeConverted = DataTimeMatcher.fromString(dateTime);

        return dateTimeConverted != null;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("LocalDateTime should be valid");
      }

      @Override
      protected void describeMismatchSafely(String dateTime, Description description) {
        description.appendText("was " + dateTime);
      }
    };
  }

  public static Matcher<String> invalid() {
    return Matchers.not(valid());
  }

  public static Matcher<String> before(LocalDateTime localDateTime) {
    return new TypeSafeMatcher<>() {
      @Override
      protected boolean matchesSafely(String dateTime) {
        LocalDateTime dateTimeConverted = DataTimeMatcher.fromString(dateTime);

        return dateTimeConverted != null && dateTimeConverted.isBefore(localDateTime);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText(
            "LocalDateTime should be before " + DataTimeMatcher.toString(localDateTime));
      }

      @Override
      protected void describeMismatchSafely(String dateTime, Description description) {
        description.appendText("was " + dateTime);
      }
    };
  }

  public static Matcher<String> after(LocalDateTime localDateTime) {
    return new TypeSafeMatcher<>() {
      @Override
      protected boolean matchesSafely(String dateTime) {
        LocalDateTime dateTimeConverted = DataTimeMatcher.fromString(dateTime);

        return dateTimeConverted != null && dateTimeConverted.isAfter(localDateTime);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText(
            "LocalDateTime should be after " + DataTimeMatcher.toString(localDateTime));
      }

      @Override
      protected void describeMismatchSafely(String dateTime, Description description) {
        description.appendText("was " + dateTime);
      }
    };
  }
}
