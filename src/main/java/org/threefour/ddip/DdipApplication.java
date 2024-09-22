package org.threefour.ddip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, RedisAutoConfiguration.class, SessionAutoConfiguration.class})
@EnableAsync
public class DdipApplication {
  public static void main(String[] args) {
    SpringApplication.run(DdipApplication.class, args);
  }
}
