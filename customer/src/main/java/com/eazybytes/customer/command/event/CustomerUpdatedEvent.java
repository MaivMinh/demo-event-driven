package com.eazybytes.customer.command.event;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class CustomerUpdatedEvent {
  private String customerId;
  private String name;
  private String email;
  private String mobileNumber;
  private boolean activeSw;
}
