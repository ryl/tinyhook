package com.github.ryl.tinyhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.ryl.tinyhook.webhook.WebHookRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TinyhookController {
  
  private static final Logger logger = LoggerFactory.getLogger(TinyhookController.class);
  
  private final ObjectMapper objectMapper;
  
  private final WebHookRegistry registry;
  
  public TinyhookController(WebHookRegistry registry) {
    objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    this.registry = registry;
  }
  
  @PostMapping("/webhook")
  public void push(@RequestBody String event, @RequestHeader HttpHeaders headers) throws Exception {
    String json = objectMapper.writeValueAsString(objectMapper.readValue(event, ObjectNode.class));
    logger.info("Received an event\nHeaders: {}\nPayload: {}", objectMapper.writeValueAsString(headers), json);
    registry.executeWebHooks(event);
  }
  
}
