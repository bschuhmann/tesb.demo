package org.talend.ps.demo.customer.itests;

import static org.apache.karaf.tooling.exam.options.KarafDistributionOption.editConfigurationFilePut;
import static org.apache.karaf.tooling.exam.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.apache.karaf.tooling.exam.options.KarafDistributionOption.logLevel;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.scanFeatures;

import java.io.File;
import java.net.URISyntaxException;

import javax.inject.Inject;

import junit.framework.Assert;

import org.apache.karaf.tooling.exam.options.LogLevelOption;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;
import org.osgi.framework.BundleContext;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;

@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class CustomerServiceTest {
    private static final String HTTP_PORT = "9191";

    @Inject
    BundleContext bundleContext;
    
    @Inject
    CustomerService customerService;

    @Configuration
    public Option[] config() {
        MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf").version("2.3.0").type("tar.gz");
        MavenUrlReference cxfFeatures = maven().groupId("org.apache.cxf.karaf").artifactId("apache-cxf").type("xml").classifier("features").version("2.6.0");
        MavenArtifactUrlReference tesbFeatures = maven().groupId("org.talend.esb").artifactId("features").type("xml").version("5.1.2");
        MavenArtifactUrlReference customerFeatures = maven().groupId("org.talend.ps.demo").artifactId("customer.features").type("xml").version("0.1-SNAPSHOT");
        
        return new Option[]{
            karafDistributionConfiguration().frameworkUrl(karafUrl).karafVersion("2.3.0").name("Apache Karaf").unpackDirectory(new File("target/exam")),            
            logLevel(LogLevelOption.LogLevel.INFO),
            scanFeatures(cxfFeatures, "cxf"),
            scanFeatures(tesbFeatures, "tesb-locator-client").start(),
            scanFeatures(customerFeatures, "customer-provider-remote").start(),
            editConfigurationFilePut("etc/org.ops4j.pax.web.cfg", "org.osgi.service.http.port", HTTP_PORT),
            //vmOption( "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005" )
        };
    }
    
    @Test
    public void testCall() throws URISyntaxException, Exception {
        Customer customer = customerService.getCustomerByName("testName");
        Assert.assertEquals(12345, customer.getCustomerId());
    }

}

