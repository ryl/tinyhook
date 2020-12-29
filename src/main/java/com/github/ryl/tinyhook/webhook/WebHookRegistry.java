package com.github.ryl.tinyhook.webhook;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@AllArgsConstructor
public class WebHookRegistry {
  
  private final List<WebHook> webHooks;
  
  public void executeWebHooks(String event) {
    webHooks.stream()
        .filter(w -> w.test(event))
        .forEach(w -> w.accept(event));
  }
  
}
