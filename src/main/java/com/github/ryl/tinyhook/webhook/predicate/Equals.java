package com.github.ryl.tinyhook.webhook.predicate;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;


@AllArgsConstructor
public class Equals implements Predicate<String> {

  private static final Logger logger = LoggerFactory.getLogger(Equals.class);
  
  private final String expression;
  
  private final String value;

  @Override
  public boolean test(String s) {
    boolean result = JsonPath.read(s, expression).equals(value);
    logger.info("{} eq {} -> {}", expression, value, result);
    return result;
  }
  
}
