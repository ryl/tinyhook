package com.github.ryl.tinyhook.webhook.predicate;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Predicate;


@AllArgsConstructor
public class Equals implements Predicate<String> {

  private static final Logger logger = LoggerFactory.getLogger(Equals.class);
  
  private final String expression;
  
  private final String value;

  @Override
  public boolean test(String s) {
    var actual = JsonPath.read(s, expression);
    boolean result = Objects.equals(actual, value);

    if (result)
      logger.info("{} eq {} -> true", expression, value);
    else
      logger.info("{} eq {} -> false, actual value = {}", expression, value, actual);
    return result;
  }
  
}
