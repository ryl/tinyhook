package com.github.ryl.tinyhook.webhook.predicate;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;

import java.util.function.Predicate;


@AllArgsConstructor
public class Equals implements Predicate<String> {
  
  private final String expression;
  
  private final String value;

  @Override
  public boolean test(String s) {
    return JsonPath.read(s, expression).equals(value);
  }
  
}
