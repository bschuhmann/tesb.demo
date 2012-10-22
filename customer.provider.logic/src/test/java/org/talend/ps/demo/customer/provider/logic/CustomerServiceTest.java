package org.talend.ps.demo.customer.provider.logic;

import org.junit.Assert;
import org.junit.Test;
import org.talend.ps.demo.customer.provider.logic.SimpleCustomerService;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;
import com.example.customerservice.NoSuchCustomerException;


public class CustomerServiceTest {
	@Test
	public void testLocal() throws NoSuchCustomerException {
		CustomerService service = new SimpleCustomerService();
		Customer customer = service.getCustomerByName("Test");
		Assert.assertEquals("Test", customer.getName());
	}
}
