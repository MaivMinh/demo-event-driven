package com.eazybytes.customer;

import com.eazybytes.customer.command.interceptor.CustomerCommandInterceptor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class CustomersApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomersApplication.class, args);
  }

  @Autowired
  public void registerCustomerCommandInterceptor(ApplicationContext context, CommandGateway commandGateway) {
    commandGateway.registerDispatchInterceptor(context.getBean(CustomerCommandInterceptor.class));
  }

  @Autowired
  public void configure(EventProcessingConfigurer configurer) {
    configurer.registerListenerInvocationErrorHandler("customer-group",
            conf -> PropagatingErrorHandler.instance()  /// Propagate the error to the top layer or caller. Maybe to the aggregate or command handler.
    );
  }
}
