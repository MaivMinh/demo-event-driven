package com.eazybytes.customer.command.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CustomerDeletedEvent {
  private String customerId;
  private boolean isActiveSw;
}
