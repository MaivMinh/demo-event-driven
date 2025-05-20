package com.eazybytes.customer.command;

import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCustomerCommand {
  @TargetAggregateIdentifier
  private String customerId;
  private boolean activeSw;
}
