package org.talend.ps.demo.customer.application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;
import com.example.customerservice.NoSuchCustomerException;

public class CustomerApplication {
	private static Logger LOG = LoggerFactory.getLogger(CustomerApplication.class);
	private CustomerService customerService;
	private boolean shouldStop;

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public void start() {
		LOG.info("Starting customer application");
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new Runnable() {
			@Override
			public void run() {
				doCalls();
			}
		});
	}
	
	public void stop() {
		LOG.info("Stopping customer application");
		shouldStop = true;
	}

	public void doCalls() {
		while (!shouldStop) {
			doCall();
			if (!shouldStop) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					shouldStop = true;
				}
			}
		}
	}

	private void doCall() {
		try {
			Customer customer = customerService
					.getCustomerByName("testName");
			LOG.info("Got reply from customerService with customer: " + customer.getCustomerId());
		} catch (Exception e) {
			LOG.error("Cannot get customer: " + e.getMessage(), e);
		}
	}

}
