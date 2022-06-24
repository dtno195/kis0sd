package com.sd.broker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author D
 */
@RestController
@RequestMapping("/broker")
public class BrokerController {
  @GetMapping("/message")
  public String test() {
    return "Hello, this is broker service";
  }
}
