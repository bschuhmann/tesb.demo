package org.talend.ps.demo.customer.itests;

import static org.apache.karaf.tooling.exam.options.KarafDistributionOption.editConfigurationFilePut;
import static org.apache.karaf.tooling.exam.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.apache.karaf.tooling.exam.options.KarafDistributionOption.logLevel;
import static org.apache.karaf.tooling.exam.options.KarafDistributionOption.replaceConfigurationFile;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.scanFeatures;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.inject.Inject;

import junit.framework.Assert;

import org.apache.karaf.tooling.exam.options.LogLevelOption;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.ExamSystem;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.TimeoutException;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.ops4j.pax.exam.spi.PaxExamRuntime;
import org.ops4j.pax.exam.spi.reactors.EagerSingleStagedReactorFactory;
import org.osgi.framework.BundleContext;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;

@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(EagerSingleStagedReactorFactory.class)
public class CustomerServiceTestDOSGi {
    private static final String HTTP_PORT = "9191";
    private static final String HTTP_PORT2 = "9192";
    
    static MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf").version("2.3.0").type("tar.gz");
    static MavenUrlReference cxfFeatures = maven().groupId("org.apache.cxf.karaf").artifactId("apache-cxf").type("xml").classifier("features").version("2.7.0");
    static MavenUrlReference dosgiFeatures = maven().groupId("org.apache.cxf.dosgi").artifactId("cxf-dosgi").type("xml").classifier("features").version("1.4-SNAPSHOT");

    @Inject
    BundleContext bundleContext;
    
    @Inject
    CustomerService customerService;

    private static TestContainer remoteContainer;
    
    public static void startRemote() {
        ExamSystem testSystem;
        try {
            testSystem = PaxExamRuntime.createTestSystem(remoteConfig());
            remoteContainer = PaxExamRuntime.createContainer(testSystem);
            remoteContainer.start();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public static void stopRemote() throws TimeoutException, IOException {
        remoteContainer.stop();
    }

    public static Option[] remoteConfig() {
        return new Option[]{
                karafDistributionConfiguration().frameworkUrl(karafUrl).karafVersion("2.2.9").name("Server").unpackDirectory(new File("target/server")).useDeployFolder(false),            
                logLevel(LogLevelOption.LogLevel.INFO),
                editConfigurationFilePut("etc/org.apache.karaf.features.cfg", "featuresBoot", "config"),
                editConfigurationFilePut("etc/org.apache.cxf.dosgi.discovery.zookeeper.server.cfg", "clientPort", "2181"),
                editConfigurationFilePut("etc/org.apache.cxf.dosgi.discovery.zookeeper.cfg", "zookeeper.host", "localhost"),
                editConfigurationFilePut("etc/org.apache.cxf.dosgi.discovery.zookeeper.cfg", "zookeeper.port", "2181"),
                editConfigurationFilePut("etc/org.ops4j.pax.web.cfg", "org.osgi.service.http.port", HTTP_PORT2),
                editConfigurationFilePut("etc/org.apache.karaf.shell.cfg", "sshPort", "8082"),
                editConfigurationFilePut("etc/org.apache.karaf.management.cfg", "rmiRegistryPort", "1100"),
                editConfigurationFilePut("etc/org.apache.karaf.management.cfg", "rmiServerPort", "44445"),
                replaceConfigurationFile("etc/jre.properties", new File("target/test-classes/jre.properties.cxf")),

                scanFeatures(cxfFeatures, "cxf"),
                scanFeatures(dosgiFeatures, "cxf-dosgi-discovery-distributed", "cxf-dosgi-zookeeper-server").start(),
                mavenBundle(maven().groupId("org.talend.ps.demo").artifactId("customer.common").version("0.1-SNAPSHOT")),
                mavenBundle(maven().groupId("org.talend.ps.demo").artifactId("customer.provider.logic").version("0.1-SNAPSHOT")),
        };
    }

    @Configuration
    public Option[] config() {
        startRemote();
        return new Option[]{
            karafDistributionConfiguration().frameworkUrl(karafUrl).karafVersion("2.2.9").name("Client").unpackDirectory(new File("target/client")).useDeployFolder(false),            
            logLevel(LogLevelOption.LogLevel.INFO),
            editConfigurationFilePut("etc/org.apache.karaf.features.cfg", "featuresBoot", "config"),
            editConfigurationFilePut("etc/org.apache.cxf.dosgi.discovery.zookeeper.cfg", "zookeeper.host", "localhost"),
            editConfigurationFilePut("etc/org.apache.cxf.dosgi.discovery.zookeeper.cfg", "zookeeper.port", "2181"),
            editConfigurationFilePut("etc/org.ops4j.pax.web.cfg", "org.osgi.service.http.port", HTTP_PORT),
            replaceConfigurationFile("etc/jre.properties", new File("target/test-classes/jre.properties.cxf")),

            scanFeatures(cxfFeatures, "cxf-core"),
            scanFeatures(dosgiFeatures, "cxf-dosgi-discovery-distributed").start(),
            mavenBundle(maven().groupId("org.talend.ps.demo").artifactId("customer.common").version("0.1-SNAPSHOT")),
            //vmOption( "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005" )
        };
    }
    
    @Test
    public void testCall() throws URISyntaxException, Exception {
        Customer customer = customerService.getCustomerByName("testName");
        Assert.assertEquals(12345, customer.getCustomerId());
    }

}

