package com.sd.broker.controller;

import com.sd.broker.dto.BrokerVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author D
 */
@RestController
@RequestMapping("/broker")
public class BrokerController {
  @GetMapping("/all")
  public ResponseEntity<List<BrokerVo>> getAllBroker() {
    List<BrokerVo> brokerVos = new ArrayList<>();
    brokerVos.add(new BrokerVo("Satama", "sssss"));
    brokerVos.add(new BrokerVo("Luffy", "aaaaa"));

    return ResponseEntity.ok(brokerVos);
  }
}
