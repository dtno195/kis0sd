package com.sd.broker;

import com.sd.broker.config.KafkaTopicConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SdBrokerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SdBrokerApplication.class, args);
  }

}
