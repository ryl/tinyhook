package com.github.ryl.tinyhook.webhook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@AllArgsConstructor
public class WebHookRegistry {

  private static final Logger logger = LoggerFactory.getLogger(WebHookRegistry.class);

  @Getter
  private final List<WebHook> webHooks;
  
  public void executeWebHooks(String event) {
    logger.info("Checking {} webhooks for a match...", webHooks.size());
    webHooks.stream()
        .filter(w -> w.test(event))
        .forEach(w -> w.accept(event));
  }
  
}
