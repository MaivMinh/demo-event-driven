package com.eazybytes.customer.service;

import com.eazybytes.customer.command.event.CustomerUpdatedEvent;
import com.eazybytes.customer.dto.CustomerDto;
import com.eazybytes.customer.entity.Customer;

public interface ICustomerService {

    /**
     * @param customer - Customer Object
     */
    void createCustomer(Customer customer);

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    CustomerDto fetchCustomer(String mobileNumber);

    /**
     * @param event - Event Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateCustomer(CustomerUpdatedEvent event);

    /**
     * @param customerId - Input Customer ID
     * @return boolean indicating if the delete of Customer details is successful or not
     */
    boolean deleteCustomer(String customerId);
}
