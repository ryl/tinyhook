package com.github.ryl.tinyhook.webhook.predicate;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Predicate;

@AllArgsConstructor
public class Exists implements Predicate<String> {

  private static final Logger logger = LoggerFactory.getLogger(Exists.class);

  private final String expression;

  @Override
  public boolean test(String s) {
    var result = Objects.nonNull(JsonPath.read(s, expression));
    logger.info("{} exists -> {}", expression, result);
    return result;
  }
  
}
