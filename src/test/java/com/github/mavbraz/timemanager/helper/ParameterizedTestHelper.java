package com.github.mavbraz.timemanager.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

/**
 * https://stackoverflow.com/questions/53327761/is-it-possible-to-nest-junit-5-parameterized-tests
 */
public class ParameterizedTestHelper {
  public static Stream<Arguments> cartesian(Stream<Arguments> a, Stream<Arguments> b) {
    List<Arguments> argumentsA = a.collect(Collectors.toList());
    List<Arguments> argumentsB = b.collect(Collectors.toList());

    List<Arguments> result = new ArrayList<>();
    for (Object o : argumentsA) {
      Object[] objects = asArray(o);
      for (Object o1 : argumentsB) {
        Object[] objects1 = asArray(o1);
        Object[] arguments = Stream.of(objects, objects1).flatMap(Stream::of).toArray();
        result.add(Arguments.of(arguments));
      }
    }
    return result.stream();
  }

  public static Stream<Arguments> join(Stream<Arguments> stream) {
    return Stream.of(Arguments.of(stream.map(Arguments::get).flatMap(Arrays::stream).collect(Collectors.toList())));
  }

  private static Object[] asArray(Object o) {
    Object[] objects;
    if (o instanceof Arguments) {
      objects = ((Arguments) o).get();
    } else {
      objects = new Object[] {o};
    }
    return objects;
  }
}
