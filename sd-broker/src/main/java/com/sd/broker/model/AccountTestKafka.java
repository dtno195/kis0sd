package com.sd.broker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author D
 */
@Data
@NoArgsConstructor
public class AccountTestKafka {
  private String holder;
  private String funds;

  public AccountTestKafka(
          @JsonProperty String holder,
          @JsonProperty String funds) {
    this.holder = holder;
    this.funds = funds;
  }
}
