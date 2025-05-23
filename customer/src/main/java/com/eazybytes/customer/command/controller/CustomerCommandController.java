package com.eazybytes.customer.command.controller;

import com.eazybytes.customer.command.CreateCustomerCommand;
import com.eazybytes.customer.command.DeleteCustomerCommand;
import com.eazybytes.customer.command.UpdateCustomerCommand;
import com.eazybytes.customer.constants.CustomerConstants;
import com.eazybytes.customer.dto.CustomerDto;
import com.eazybytes.customer.dto.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class CustomerCommandController {
  private final CommandGateway commandGateway;

  @PostMapping("/create")
  public ResponseEntity<ResponseDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {

    CreateCustomerCommand command = CreateCustomerCommand.builder()
            .customerId(UUID.randomUUID().toString())
            .name(customerDto.getName())
            .email(customerDto.getEmail())
            .mobileNumber(customerDto.getMobileNumber())
            .activeSw(CustomerConstants.ACTIVE_SW)
            .build();
    commandGateway.sendAndWait(command, 20000, TimeUnit.MILLISECONDS);
    return ResponseEntity
            .status(org.springframework.http.HttpStatus.CREATED)
            .body(new ResponseDto(CustomerConstants.STATUS_201, CustomerConstants.MESSAGE_201));
  }


  @PutMapping("/update")
  public ResponseEntity<ResponseDto> updateCustomerDetails(@Valid @RequestBody CustomerDto customerDto) {
    UpdateCustomerCommand command = UpdateCustomerCommand.builder()
            .customerId(customerDto.getCustomerId())
            .name(customerDto.getName())
            .email(customerDto.getEmail())
            .mobileNumber(customerDto.getMobileNumber())
            .activeSw(CustomerConstants.ACTIVE_SW)
            .build();
    commandGateway.sendAndWait(command, 20000, TimeUnit.MILLISECONDS);

    return ResponseEntity
            .status(org.springframework.http.HttpStatus.OK)
            .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
  }

  @PatchMapping("/delete")
  public ResponseEntity<ResponseDto> deleteCustomer(@RequestParam("customerId")
                                                    @Pattern(regexp = "(^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$)",
                                                            message = "CustomerId is invalid") String customerId) {
    DeleteCustomerCommand command = DeleteCustomerCommand.builder()
            .customerId(customerId)
            .activeSw(CustomerConstants.IN_ACTIVE_SW)
            .build();

    commandGateway.sendAndWait(command, 20000, TimeUnit.MILLISECONDS);
    return ResponseEntity
            .status(org.springframework.http.HttpStatus.OK)
            .body(new ResponseDto(CustomerConstants.STATUS_200,
                    CustomerConstants.MESSAGE_200));
  }
}
