package com.eazybytes.customer.service.impl;

import com.eazybytes.customer.command.event.CustomerUpdatedEvent;
import com.eazybytes.customer.constants.CustomerConstants;
import com.eazybytes.customer.dto.CustomerDto;
import com.eazybytes.customer.entity.Customer;
import com.eazybytes.customer.exception.CustomerAlreadyExistsException;
import com.eazybytes.customer.exception.ResourceNotFoundException;
import com.eazybytes.customer.mapper.CustomerMapper;
import com.eazybytes.customer.repository.CustomerRepository;
import com.eazybytes.customer.service.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

  private CustomerRepository customerRepository;

  @Override
  public void createCustomer(Customer customer) {
    /// Việc validation lại lần nữa để đảm bảo rằng không có trường hợp nào mà chúng ta lại tạo mới một Customer với cùng một số điện thoại.
    /// Vì chúng ta sẽ không chắc là message broker bị lỗi hay vì lý do gì khiến cho hệ thống bị trùng event.
    Optional<Customer> optionalCustomer = customerRepository.findByMobileNumberAndActiveSw(
            customer.getMobileNumber(), true);
    if (optionalCustomer.isPresent()) {
      throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
              + customer.getMobileNumber());
    }
    customer.setActiveSw(CustomerConstants.ACTIVE_SW);
    Customer savedCustomer = customerRepository.save(customer);
  }

  @Override
  public CustomerDto fetchCustomer(String mobileNumber) {
    Customer customer = customerRepository.findByMobileNumberAndActiveSw(mobileNumber, true).orElseThrow(
            () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
    );
    return CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
  }

  @Override
  public boolean updateCustomer(CustomerUpdatedEvent event) {
    try {
      Customer customer = customerRepository.findByMobileNumberAndActiveSw(event.getMobileNumber(), true).orElseThrow(
              () -> new ResourceNotFoundException("Customer", "mobileNumber", event.getMobileNumber())
      );
      CustomerMapper.mapEventToCustomer(event, customer);
      customerRepository.save(customer);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean deleteCustomer(String customerId) {
    Customer customer = customerRepository.findById(customerId).orElseThrow(
            () -> new ResourceNotFoundException("Customer", "customerId", customerId)
    );
    customer.setActiveSw(CustomerConstants.IN_ACTIVE_SW);
    System.out.println(customer.isActiveSw());
    customerRepository.save(customer);
    return true;
  }

}
