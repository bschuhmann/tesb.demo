features:install tesb-zookeeper-server tesb-locator-soap-service 
features:addurl mvn:org.talend.ps.demo/customer.features/0.1-SNAPSHOT/xml
features:install customer-provider-remote
features:install customer-application-remote

# Setup for container that hosts the zookeeper server
install -s mvn:org.apache.zookeeper/zookeeper/3.3.1
install -s mvn:org.apache.cxf.dosgi/cxf-dosgi-ri-discovery-distributed-zookeeper-server/1.3.1

Create config "etc/org.apache.cxf.dosgi.discovery.zookeeper.server.cfg" with the following content:
zookeeper.host:127.0.0.1
clientPort:2181
zookeeper.port:2181

# dosgi setup on karaf. Will be needed for container 1 and 2
features:chooseurl cxf 2.7.0
features:install cxf
install -s mvn:org.osgi/org.osgi.enterprise/4.2.0
install -s mvn:org.apache.servicemix.bundles/org.apache.servicemix.bundles.jdom/1.1_3
install -s mvn:org.apache.cxf.dosgi/cxf-dosgi-ri-topology-manager/1.3.1
install -s mvn:org.apache.cxf.dosgi/cxf-dosgi-ri-dsw-cxf/1.3.1
install -s mvn:org.apache.cxf.dosgi/cxf-dosgi-ri-discovery-local/1.3.1
install -s mvn:org.apache.zookeeper/zookeeper/3.3.1
install -s mvn:org.apache.cxf.dosgi/cxf-dosgi-ri-discovery-distributed/1.3.1

# Container 1: CustomerService provider
install -s mvn:org.talend.ps.demo/customer.common/0.1-SNAPSHOT
install -s mvn:org.talend.ps.demo/customer.provider.logic/0.1-SNAPSHOT

# Container 2: CustomerService consumer
install -s mvn:org.talend.ps.demo/customer.common/0.1-SNAPSHOT
install -s mvn:org.talend.ps.demo/customer.application/0.1-SNAPSHOT

