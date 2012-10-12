package org.talend.ps.demo.customer.provider.logic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;
import com.example.customerservice.CustomerType;
import com.example.customerservice.NoSuchCustomerException;

public class SimpleCustomerService implements CustomerService {
	private static Logger LOG = LoggerFactory.getLogger(SimpleCustomerService.class);

	@Override
	public Customer getCustomerByName(String name)
			throws NoSuchCustomerException {
		LOG.info("Called getCustomerByName for: " + name);
		Customer customer = new Customer();
		customer.setName(name);
		customer.setCustomerId(12345);
		customer.setType(CustomerType.PRIVATE);
		return customer;
	}

	@Override
	public List<Customer> getCustomersByName(String name)
			throws NoSuchCustomerException {
		LOG.info("Called getCustomersByName for: " + name);
		Customer customer = new Customer();
		customer.setName(name);
		customer.setCustomerId(12345);
		customer.setType(CustomerType.PRIVATE);
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(customer);
		return customers;
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		LOG.info("Called updateCustomer for: " + customer.getName());
		return customer;
	}

}
