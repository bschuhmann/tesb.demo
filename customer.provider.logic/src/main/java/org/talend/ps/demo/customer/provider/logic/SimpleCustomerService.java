package org.talend.ps.demo.customer.provider.logic;

import java.util.ArrayList;
import java.util.List;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;
import com.example.customerservice.CustomerType;
import com.example.customerservice.NoSuchCustomerException;

public class SimpleCustomerService implements CustomerService {

	@Override
	public Customer getCustomerByName(String name)
			throws NoSuchCustomerException {
		System.out.println("Called getCustomerByName for: " + name);
		Customer customer = new Customer();
		customer.setName(name);
		customer.setCustomerId(12345);
		customer.setType(CustomerType.PRIVATE);
		return customer;
	}

	@Override
	public List<Customer> getCustomersByName(String name)
			throws NoSuchCustomerException {
		System.out.println("Called getCustomersByName for: " + name);
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
		System.out.println("Called updateCustomer for: " + customer.getName());
		return customer;
	}

}
