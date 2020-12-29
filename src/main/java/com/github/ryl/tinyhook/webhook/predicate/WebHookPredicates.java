package com.github.ryl.tinyhook.webhook.predicate;

public class WebHookPredicates {
  
  public static Equals eq(String jsonPath, String value) {
    return new Equals(jsonPath, value);
  }
  
  public static Exists exists(String jsonPath) {
    return new Exists(jsonPath);
  }
  
}
