package com.eazybytes.customer.query.handler;

import com.eazybytes.customer.dto.CustomerDto;
import com.eazybytes.customer.query.FindCustomerQuery;
import com.eazybytes.customer.repository.CustomerRepository;
import com.eazybytes.customer.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerQueryHandler {
  private final CustomerRepository customerRepository;
  private final ICustomerService customerService;

  @QueryHandler
  public CustomerDto findCustomer(FindCustomerQuery query) {
    return customerService.fetchCustomer(query.getMobileNumber());
  }
}
