package com.eazybytes.customer.command;

import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class UpdateCustomerCommand {
  @TargetAggregateIdentifier
  private String customerId;
  private String name;
  private String email;
  private String mobileNumber;
  private boolean activeSw;
}
