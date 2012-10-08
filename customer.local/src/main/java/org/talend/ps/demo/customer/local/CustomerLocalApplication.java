package org.talend.ps.demo.customer.local;

import org.talend.ps.demo.customer.provider.logic.SimpleCustomerService;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;
import com.example.customerservice.NoSuchCustomerException;

public class CustomerLocalApplication 
{
    public static void main( String[] args )
    {
        CustomerService customerService = new SimpleCustomerService();
        
        try {
        	Customer customer = customerService.getCustomerByName("Heinz");
	        
	        System.out.printf("%s %s%n", customer.getCustomerId(), customer.getName());
		} catch (NoSuchCustomerException e) {
			System.out.println("No such customer");
		}
    }
}
