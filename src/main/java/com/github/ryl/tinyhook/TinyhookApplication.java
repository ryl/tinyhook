package com.github.ryl.tinyhook;

import com.github.ryl.tinyhook.webhook.WebHook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.ryl.tinyhook.consumers.WebHookConsumers.exec;
import static com.github.ryl.tinyhook.webhook.predicate.WebHookPredicates.eq;
import static com.github.ryl.tinyhook.webhook.predicate.WebHookPredicates.exists;

@Configuration
@SpringBootApplication
public class TinyhookApplication {

  public static void main(String[] args) {
    SpringApplication.run(TinyhookApplication.class, args);
  }

  @Bean
  public WebHook webHook() {
    return new WebHook(
        eq("$['repository']['full_name']", "ryl/tinyhook").and(exists("$['commits']")),
        exec("/opt/tinyhook/data", "rm", "-rf", "/opt/tinyhook/data/tinyhook")
            .andThen(exec("/opt/tinyhook/data", "git", "clone", "git@github.com:ryl/tinyhook.git"))
            .andThen(exec("/opt/tinyhook/data/tinyhook", "./gradlew", "build", "-x", "test")));
  }
  
}
