package com.github.ryl.tinyhook.webhook;

import lombok.AllArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Predicate;

@AllArgsConstructor
public class WebHook implements Predicate<String>, Consumer<String> {
     
  private final Predicate<String> predicate;
  
  private final Consumer<String> consumer;
  
  @Override
  public boolean test(String json) {
    return predicate.test(json);
  }

  @Override
  public void accept(String s) {
    consumer.accept(s);    
  }
  
}
