package com.eazybytes.customer.command.event;

import lombok.*;

/**
 * N + V(past tense) + Event.
 */

@Getter @Setter
@NoArgsConstructor  @AllArgsConstructor
@Builder
public class CustomerCreatedEvent {
  private String customerId;
  private String name;
  private String mobileNumber;
  private String email;
  private boolean activeSw;
}
