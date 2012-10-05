package org.talend.ps.demo.customer.application;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;
import com.example.customerservice.NoSuchCustomerException;

public class CustomerApplication {
	private CustomerService customerService;

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
		

	public void startUp() {
		System.out.println("Customer application will send request in 5 seconds ...");
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					Customer customer = customerService.getCustomerByName("testName");
					System.out.println("Get customer: " + customer.getCustomerId());
				} catch (NoSuchCustomerException e) {
					e.printStackTrace();
					throw new RuntimeException("Cannot get customer: " + e.getMessage(), e);
				}
			}
		}, 5, TimeUnit.SECONDS);
	}
	
}
