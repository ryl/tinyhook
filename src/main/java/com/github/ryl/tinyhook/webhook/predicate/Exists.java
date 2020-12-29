package com.github.ryl.tinyhook.webhook.predicate;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.function.Predicate;

@AllArgsConstructor
public class Exists implements Predicate<String> {

  private final String expression;

  @Override
  public boolean test(String s) {
    return Objects.nonNull(JsonPath.read(s, expression));
  }
  
}
