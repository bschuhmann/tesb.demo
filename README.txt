features:install tesb-zookeeper-server tesb-locator-soap-service 
features:addurl mvn:org.talend.ps.demo/customer.features/0.1-SNAPSHOT/xml
features:install customer-provider-remote
features:install customer-application-remote

# dosgi setup on karaf. Will be needed for container 1 and 2
#-----------------------------------------------------------
features:addurl mvn:org.apache.cxf.dosgi/cxf-dosgi/1.4-SNAPSHOT/xml/features
features:install cxf-dosgi-discovery-distributed

Create config "etc/org.apache.cxf.dosgi.discovery.zookeeper.cfg" with the following content:
zookeeper.port=2181
zookeeper.host=localhost

# Setup for container that hosts the zookeeper server
#----------------------------------------------------
features:install cxf-dosgi-zookeeper-server

Create config "etc/org.apache.cxf.dosgi.discovery.zookeeper.server.cfg" with the following content:
zookeeper.host:127.0.0.1
clientPort:2181
zookeeper.port:2181

# Container 1: CustomerService provider
#--------------------------------------
features:addurl mvn:org.apache.cxf.dosgi/cxf-dosgi/1.4-SNAPSHOT/xml/features
features:install cxf-dosgi-discovery-distributed
features:install cxf-dosgi-zookeeper-server
install -s mvn:org.talend.ps.demo/customer.common/0.1-SNAPSHOT
install -s mvn:org.talend.ps.demo/customer.provider.logic/0.1-SNAPSHOT

# Container 2: CustomerService consumer
#--------------------------------------
features:addurl mvn:org.apache.cxf.dosgi/cxf-dosgi/1.4-SNAPSHOT/xml/features
features:install cxf-dosgi-discovery-distributed
install -s mvn:org.talend.ps.demo/customer.common/0.1-SNAPSHOT
install -s mvn:org.talend.ps.demo/customer.application/0.1-SNAPSHOT
