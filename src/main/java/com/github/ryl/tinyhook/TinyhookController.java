package com.github.ryl.tinyhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TinyhookController {
  
  private static final Logger logger = LoggerFactory.getLogger(TinyhookController.class);
  
  private final ObjectMapper objectMapper;
  
  public TinyhookController() {
    objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }
  
  @PostMapping("/webhook")
  public void push(@RequestBody Map<String, Object> push, @RequestHeader HttpHeaders headers) throws Exception {
    String json = objectMapper.writeValueAsString(push);
    Object commits = JsonPath.read(json, "$['commits']");
    String name = JsonPath.read(json, "$['repository']['full_name']");
    
    logger.info("Received a push\nHeaders: {}\nPayload: {}", objectMapper.writeValueAsString(headers), json);
    logger.info("ry/notes = {}", name.equals("ryl/notes"));
    logger.info("commits = {}", commits != null);
  }
  
}
