/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.common;

import java.net.InetAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.Transferable;

/**
 *
 * @author manikantans
 */
public class IgniteContainerInitializer implements BeforeAllCallback, ParameterResolver, Store.CloseableResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(IgniteContainerInitializer.class);
    
    private static AtomicReference<GenericContainer> igniteContainerRef;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        synchronized (context) {
            HttpWaitStrategy waitStrategy = Wait.forHttp("/ignite?cmd=version")
                    .forStatusCode(200)
                    .forPort(8080)
                    .withReadTimeout(Duration.ofSeconds(120));
            Transferable configFile = Transferable.of(getConfigFile(), 0777);
            GenericContainer container = new IgniteContainer().withExposedPorts(8080, 10800, 47500, 47100)
                    .waitingFor(waitStrategy)
                    .withCopyToContainer(configFile, "/opt/ignite/apache-ignite/config/test-config.xml")
                    .withCommand(getEntryPoint())
                    .withReuse(true);
            
            container.start();
            LOGGER.debug("8080 Mapped port {}",container.getMappedPort(8080));
            LOGGER.debug("10800 Mapped port {}",container.getMappedPort(10800));
            LOGGER.debug("47500 Mapped port {}",container.getMappedPort(47500));
            LOGGER.debug("47100 Mapped port {}",container.getMappedPort(47100));
            igniteContainerRef = new AtomicReference<>(container);
            ExtensionContext rootContext = context.getRoot();
            Store contextStore = rootContext.getStore(ExtensionContext.Namespace.GLOBAL);
            contextStore.put("ignite-container", igniteContainerRef);
            context.publishReportEntry("ignite-container");
            context.notifyAll();
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return GenericContainer.class.equals(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        ExtensionContext root = extensionContext.getRoot();
        Store rootStore = root.getStore(ExtensionContext.Namespace.GLOBAL);
        AtomicReference<GenericContainer> ref = rootStore.get("ignite-container", AtomicReference.class);
        return ref.get();
    }

    @Override
    public void close() throws Throwable {
        igniteContainerRef.get().stop();
    }

    private String getConfigFile() {
        return """
               <?xml version="1.0" encoding="UTF-8"?>
               <beans xmlns="http://www.springframework.org/schema/beans"
                      xmlns:util="http://www.springframework.org/schema/util"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xsi:schemaLocation="
                   http://www.springframework.org/schema/beans
                   http://www.springframework.org/schema/beans/spring-beans.xsd
                   http://www.springframework.org/schema/util
                   http://www.springframework.org/schema/util/spring-util.xsd">
                   <bean class="org.apache.ignite.configuration.IgniteConfiguration">
                       <property name="workDirectory" value="/tmp/ignite/work"/>
                       <property name="peerClassLoadingEnabled" value="true"/>
                        <property name="clientConnectorConfiguration">
                            <bean class="org.apache.ignite.configuration.ClientConnectorConfiguration">
                               <property name="port" value="10800"/>
                               <property name="thinClientEnabled" value="true"/>
                            </bean>
                        </property> 
                      <property name="discoverySpi">
                           <bean class="org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi">
                               <property name="localPort" value="47500"/>
                               <property name="ipFinder">
                                   <bean class="org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder">
                                       <property name="addresses">
                                           <value>LOCAL_ADDRESS:47500</value>
                                       </property>
                                   </bean>
                               </property>
                           </bean>    
                       </property>
                       <property name="dataStorageConfiguration">
                           <bean class="org.apache.ignite.configuration.DataStorageConfiguration">
                               <property name="dataRegionConfigurations">
                                   <list>
                                       <bean class="org.apache.ignite.configuration.DataRegionConfiguration">
                                           <property name="name" value="somecache"/>
                                           <property name="metricsEnabled" value="true"/>
                                           <property name="initialSize" value="#{1024L * 1024L* 512}"/>
                                           <property name="maxSize" value="#{1024L * 1024L*512}"/>
                                           <property name="pageEvictionMode" value="RANDOM_2_LRU"/>
                                       </bean>
                                   </list>
                               </property>
                           </bean>
                       </property>
                       <property name="cacheConfiguration">
                           <list>
                               <bean class="org.apache.ignite.configuration.CacheConfiguration">
                                   <property name="name" value="somecache"/>
                                   <property name="cacheMode" value="REPLICATED"/>
                                   <property name="statisticsEnabled" value="true"/>
                                   <property name="dataRegionName" value="somecache"/>
                               </bean>
                           </list>
                       </property>
                   </bean>
               </beans>
               """.replace("LOCAL_ADDRESS", InetAddress.getLoopbackAddress().getHostAddress());
    }

    private static String[] getEntryPoint() {
        List<String> cmdArgs = List.of(
                "/opt/java/openjdk/bin/java",
                //                "/opt/jre17/bin/java",
//                "--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED",
//                "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
//                "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
//                "--add-opens=java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED",
//                "--add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED",
//                "--add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED",
//                "--add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED",
//                "--add-opens=java.base/java.io=ALL-UNNAMED",
//                "--add-opens=java.base/java.nio=ALL-UNNAMED",
//                "--add-opens=java.base/java.util=ALL-UNNAMED",
//                "--add-opens=java.base/java.lang=ALL-UNNAMED",
//                "--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED",
//                "--add-opens=java.base/java.net=ALL-UNNAMED",
                "-Xms1g",
                "-Xmx1g",
                "-server",
                "-XX:MaxMetaspaceSize=256m",
                "-DIGNITE_HOME=/opt/ignite/apache-ignite",
                "-DIGNITE_QUIET=false",
                "-cp",
                "/opt/ignite/apache-ignite/libs/*:/opt/ignite/apache-ignite/libs/*:/opt/ignite/apache-ignite/libs/ignite-control-utility/*:/opt/ignite/apache-ignite/libs/ignite-indexing/*:/opt/ignite/apache-ignite/libs/ignite-spring/*:/opt/ignite/apache-ignite/libs/licenses/*:/opt/ignite/apache-ignite/libs/optional/ignite-kubernetes/*:/opt/ignite/apache-ignite/libs/optional/ignite-rest-http/*:/opt/ignite/apache-ignite/libs/optional/ignite-log4j2/*",
                "org.apache.ignite.startup.cmdline.CommandLineStartup",
                "/opt/ignite/apache-ignite/config/test-config.xml");
        return cmdArgs.toArray(String[]::new);

    }
}
