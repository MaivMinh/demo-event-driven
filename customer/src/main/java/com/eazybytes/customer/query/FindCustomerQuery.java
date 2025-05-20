package com.eazybytes.customer.query;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FindCustomerQuery {
  private String mobileNumber;
}
