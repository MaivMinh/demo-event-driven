package com.eazybytes.customer.query.controller;


import com.eazybytes.customer.dto.CustomerDto;
import com.eazybytes.customer.query.FindCustomerQuery;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class CustomerQueryController {
  private final QueryGateway queryGateway;

  @GetMapping("/fetch")
  public ResponseEntity<CustomerDto> fetchCustomerDetails(@RequestParam("mobileNumber")
                                                          @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                          String mobileNumber) {
    FindCustomerQuery query = FindCustomerQuery.builder()
            .mobileNumber(mobileNumber)
            .build();
    CustomerDto result = queryGateway.query(query, ResponseTypes.instanceOf(CustomerDto.class)).join();
    return ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(result);
  }
}
