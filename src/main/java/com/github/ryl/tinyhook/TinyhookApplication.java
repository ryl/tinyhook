package com.github.ryl.tinyhook;

import com.github.ryl.tinyhook.webhook.WebHook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.ryl.tinyhook.webhook.predicate.WebHookPredicates.eq;
import static com.github.ryl.tinyhook.webhook.predicate.WebHookPredicates.exists;

@Configuration
@SpringBootApplication
public class TinyhookApplication {

  public static void main(String[] args) {
    SpringApplication.run(TinyhookApplication.class, args);
  }

  @Bean
  WebHook webHook() {
    return new WebHook(
        eq("$['repository']['full_name']", "/ryl/notes").and(exists("$['commits']")),
        payload -> {
          // TODO: Do something here.
        }
    );
  };
  
}
