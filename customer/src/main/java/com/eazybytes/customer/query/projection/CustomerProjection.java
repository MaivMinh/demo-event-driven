package com.eazybytes.customer.query.projection;

import com.eazybytes.customer.command.event.CustomerCreatedEvent;
import com.eazybytes.customer.command.event.CustomerDeletedEvent;
import com.eazybytes.customer.command.event.CustomerUpdatedEvent;
import com.eazybytes.customer.entity.Customer;
import com.eazybytes.customer.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerProjection {
  private final ICustomerService customerService;

  @EventHandler
  public void on(CustomerCreatedEvent event) {
    /// Nếu như ở phía Command Handler thì chúng ta sử dụng @EventSourcingHandler để Store dữ liệu(trạng thái) vào trong Write Database, cũng như để publish Event vào Event Bus.
    /// Thì ở phía Query, chúng ta sẽ sử dụng @EventHandler để consume Event.

    Customer customer = new Customer();
    BeanUtils.copyProperties(event, customer);

    /// Lưu dữ liệu vào trong Read Database.
    customerService.createCustomer(customer);
  }

  @EventHandler
  public void on(CustomerUpdatedEvent event) {
    /// Cập nhật dữ liệu vào trong Read Database.
    customerService.updateCustomer(event);
  }

  @EventHandler
  public void on(CustomerDeletedEvent event) {
    /// Xóa dữ liệu trong Read Database.
    customerService.deleteCustomer(event.getCustomerId());
  }
}
