package com.eazybytes.customer.command.aggregate;

import com.eazybytes.customer.command.CreateCustomerCommand;
import com.eazybytes.customer.command.DeleteCustomerCommand;
import com.eazybytes.customer.command.UpdateCustomerCommand;
import com.eazybytes.customer.command.event.CustomerCreatedEvent;
import com.eazybytes.customer.command.event.CustomerDeletedEvent;
import com.eazybytes.customer.command.event.CustomerUpdatedEvent;
import com.eazybytes.customer.entity.Customer;
import com.eazybytes.customer.exception.CustomerAlreadyExistsException;
import com.eazybytes.customer.repository.CustomerRepository;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor
public class CustomerAggregate {

  @AggregateIdentifier
  private String customerId;
  private String name;
  private String email;
  private String mobileNumber;
  private boolean activeSw;

  /**
   * Constructor to handle Create Customer Command. That's mean we call it is Command Handler.
   */
  @CommandHandler
  public CustomerAggregate(CreateCustomerCommand command, CustomerRepository customerRepository) {
    /// Trigger the event associated with the command.
    CustomerCreatedEvent event = CustomerCreatedEvent.builder()
            .customerId(command.getCustomerId())
            .name(command.getName())
            .email(command.getEmail())
            .mobileNumber(command.getMobileNumber())
            .activeSw(command.isActiveSw())
            .build();

    /// Thực hiện Publish event đã tạo.
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(CustomerCreatedEvent event) {
    /// Một khi event được publish từ Command Handler ở phía trên, thì lúc này chúng ta sẽ catch được event này thông qua @EventSourcingHandler.
    /// Khi catch được Event này, chúng ta sẽ lưu dữ liệu vào trong Write Database.
    /// Một khi dữ liệu được lưu thành công vào trong Write Database, thì Axon Server sẽ tiến hành gửi Event vào Event Bus.

    this.customerId = event.getCustomerId();
    this.name = event.getName();
    this.email = event.getEmail();
    this.mobileNumber = event.getMobileNumber();
    this.activeSw = event.isActiveSw();
  }

  @CommandHandler
  public void handle(UpdateCustomerCommand command) {
    /// Validate the command.

    /// Create the event associated with the command.
    CustomerUpdatedEvent event = CustomerUpdatedEvent.builder()
            .customerId(command.getCustomerId())
            .name(command.getName())
            .email(command.getEmail())
            .mobileNumber(command.getMobileNumber())
            .activeSw(command.isActiveSw())
            .build();

    /// Trigger the event associated with the command to Axon Server.
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(CustomerUpdatedEvent event) {
    /// Handling the event that is triggered by the Command Handler.
    this.name = event.getName();
    this.email = event.getEmail();
  }

  @CommandHandler
  public void handle(DeleteCustomerCommand command) {
    /// Validate the command.
    /// Create the event associated with the command.
    CustomerDeletedEvent event = CustomerDeletedEvent.builder()
            .customerId(command.getCustomerId())
            .isActiveSw(false)
            .build();
    /// Trigger the event associated with the command to Axon Server.
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(CustomerDeletedEvent event) {
    /// Handling the event that is triggered by the Command Handler.
    this.customerId = event.getCustomerId();
    this.activeSw = event.isActiveSw();
  }
}
