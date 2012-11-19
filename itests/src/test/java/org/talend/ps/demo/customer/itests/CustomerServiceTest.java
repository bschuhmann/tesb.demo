package org.talend.ps.demo.customer.itests;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;
import com.example.customerservice.NoSuchCustomerException;

@RunWith(JUnit4TestRunner.class)
public class CustomerServiceTest {
    private static Logger LOG = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Inject
    CustomerService customerService;

    @Configuration
    public static Option[] configure() throws Exception {
        return new Option[] {
                mavenBundle().groupId("org.apache.aries").artifactId("org.apache.aries.util").version("1.0.0"),
                mavenBundle().groupId("org.apache.aries.proxy").artifactId("org.apache.aries.proxy.api").version("1.0.0"),
                mavenBundle().groupId("org.apache.aries.proxy").artifactId("org.apache.aries.proxy.impl").version("1.0.0"),
                mavenBundle().groupId("org.apache.aries.blueprint").artifactId("org.apache.aries.blueprint.api").version("1.0.0"),
                mavenBundle().groupId("org.apache.aries.blueprint").artifactId("org.apache.aries.blueprint.core").version("1.0.1"),
                mavenBundle().groupId("org.apache.aries.blueprint").artifactId("org.apache.aries.blueprint.compatibility").version("1.0.0").noStart(),
                mavenBundle().groupId("org.talend.ps.demo").artifactId("customer.common").versionAsInProject(),
                mavenBundle().groupId("org.talend.ps.demo").artifactId("customer.provider.logic").versionAsInProject()
                };
    }

    @Test
    public void testCall() throws NoSuchCustomerException {
        Customer customer = customerService.getCustomerByName("testName");
        LOG.info("Got reply from customerService with customer: " + customer.getCustomerId());
    }

}
