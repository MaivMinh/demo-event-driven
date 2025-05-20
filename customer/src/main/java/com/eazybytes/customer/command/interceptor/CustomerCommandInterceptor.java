package com.eazybytes.customer.command.interceptor;

import com.eazybytes.customer.command.CreateCustomerCommand;
import com.eazybytes.customer.command.DeleteCustomerCommand;
import com.eazybytes.customer.command.UpdateCustomerCommand;
import com.eazybytes.customer.entity.Customer;
import com.eazybytes.customer.exception.CustomerAlreadyExistsException;
import com.eazybytes.customer.exception.ResourceNotFoundException;
import com.eazybytes.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class CustomerCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
  private final CustomerRepository customerRepository;

  @Nonnull
  @Override
  public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
    return (index, message) -> {
      /// Intercept the command message.
      if (CreateCustomerCommand.class.equals(message.getPayloadType())) {
        /// Cast the message to CreateCustomerCommand.
        CreateCustomerCommand command = (CreateCustomerCommand) message.getPayload();
        /// Validation the command.
        Customer customer = customerRepository.findByMobileNumberAndActiveSw(command.getMobileNumber(), true).orElse(null);
        if (customer != null) {
          throw new CustomerAlreadyExistsException("Customer with mobile number " + command.getMobileNumber() + " already exists.");
        }

      } else if (UpdateCustomerCommand.class.equals(message.getPayloadType())) {
        /// Cast the message to CreateCustomerCommand.
        UpdateCustomerCommand command = (UpdateCustomerCommand) message.getPayload();
        /// Validation the command.
        Customer customer = customerRepository.findByMobileNumberAndActiveSw(command.getMobileNumber(), true).orElse(null);
        if (customer == null) {
          throw new ResourceNotFoundException("Customer", "mobileNumber", command.getMobileNumber());
        }
      } else if (DeleteCustomerCommand.class.equals(message.getPayloadType())) {
        /// Cast the message to CreateCustomerCommand.
        CreateCustomerCommand command = (CreateCustomerCommand) message.getPayload();
        /// Validation the command.
        Customer customer = customerRepository.findByMobileNumberAndActiveSw(command.getMobileNumber(), true).orElse(null);
        if (customer != null) {
          throw new CustomerAlreadyExistsException("Customer with mobile number " + command.getMobileNumber() + " already exists.");
        }
      }
      return message;
    };
  }
}
