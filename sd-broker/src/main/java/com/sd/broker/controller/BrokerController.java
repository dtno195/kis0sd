package com.sd.broker.controller;

import com.sd.broker.config.Producer;
import com.sd.broker.dto.BrokerVo;
import com.sd.broker.model.AccountTestKafka;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author D
 */
@RestController
@RequestMapping("/broker")
@Slf4j
public class BrokerController {

//  Logger logger = LoggerFactory.getLogger(BrokerController.class);
//
//  private final Producer producer;
//
//  public BrokerController(Producer producer) {
//    this.producer = producer;
//  }


//  @PostMapping("/publish")
//  public void messageToTopic(@RequestParam("message") String message){
//
//    this.producer.sendMessage(message);
//
//
//  }
}
