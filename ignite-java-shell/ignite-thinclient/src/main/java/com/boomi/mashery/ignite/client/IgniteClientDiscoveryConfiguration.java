/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.client;

import com.boomi.mashery.common.CacheClientException;
import com.boomi.mashery.common.MsgCode;
import com.boomi.mashery.common.Utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.kubernetes.configuration.KubernetesConnectionConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.kubernetes.TcpDiscoveryKubernetesIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author manikantans
 */
public class IgniteClientDiscoveryConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(IgniteClientDiscoveryConfiguration.class);
    private ClientDiscoveryType discoveryType;
    private String k8sServiceAccount;
    private String nameSpace;
    private List<String> ipAddresses;

    private IgniteClientDiscoveryConfiguration() {
    }

    public ClientDiscoveryType getDiscoveryType() {
        return discoveryType;
    }

    public String getK8sServiceAccount() {
        return k8sServiceAccount;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public List<String> getIpAddresses() {
        return ipAddresses;
    }

    
    public ClientDiscoveryType type(){
        return discoveryType;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("discoveryType=").append(discoveryType);
        sb.append(", ipAddresses=").append(ipAddresses);
        sb.append(", k8sServiceAccount=").append(k8sServiceAccount);
        sb.append(", nameSpace=").append(nameSpace);
        sb.append('}');
        return sb.toString();
    }

    
    ClientConfiguration getThinClientConfiguration() {
        ClientConfiguration config = new ClientConfiguration()
                .setAddresses(ipAddresses.toArray(String[]::new))
                .setTimeout(60000)
                .setHeartbeatEnabled(true)
                .setHeartbeatInterval(10000)
                .setClusterDiscoveryEnabled(false);
        
                
        return config;
    }

    IgniteConfiguration getIgniteConfiguration() throws CacheClientException {

        TcpDiscoverySpi columbus = new TcpDiscoverySpi();
        switch (discoveryType) {
            case K8S -> {
                KubernetesConnectionConfiguration kconfig = new KubernetesConnectionConfiguration();
                kconfig.setServiceName(k8sServiceAccount);
                kconfig.setNamespace(nameSpace);
                TcpDiscoveryIpFinder ranger = new TcpDiscoveryKubernetesIpFinder(kconfig);
                columbus.setIpFinder(ranger);
            }
            case TCP -> {
                TcpDiscoveryVmIpFinder ranger = new TcpDiscoveryVmIpFinder();
                ranger.setAddresses(ipAddresses);
                columbus.setIpFinder(ranger);
            }
            default -> {
                throw new CacheClientException(MsgCode.E0003_INVALID_CONFIG, "Unknown Configuration type " + discoveryType);
            }
        }

        String parentPath = findParentPath();
        LOGGER.debug("Parent directory is {}", parentPath);
        return new IgniteConfiguration()
                .setDiscoverySpi(columbus)
                .setClientMode(true)
                .setPeerClassLoadingEnabled(true)
                .setWorkDirectory(parentPath + "/work")
                .setIgniteHome(parentPath);
    }

    private IgniteClientDiscoveryConfiguration validate() throws CacheClientException {
        switch (discoveryType) {
            case K8S -> {
                if (StringUtils.isEmpty(nameSpace)) {
                    throw new CacheClientException(MsgCode.E0003_INVALID_CONFIG, "K8s namespace");
                }
                if (StringUtils.isEmpty(k8sServiceAccount)) {
                    throw new CacheClientException(MsgCode.E0003_INVALID_CONFIG, "K8s service account");
                }
            }
            case TCP -> {
                if (CollectionUtils.isEmpty(ipAddresses)) {
                    throw new CacheClientException(MsgCode.E0003_INVALID_CONFIG, "Missing ip addresses");
                }
            }
            default -> {
                throw new CacheClientException(MsgCode.E0003_INVALID_CONFIG, "Unknown Configuration type " + discoveryType);
            }
        }
        return this;
    }

    private String findParentPath() throws CacheClientException {
        Path tempPath = Utils.getTempDirectoryPath();
        LOGGER.debug("Using temp directory = {}", tempPath);
        Path parentPath = tempPath.resolve("ignite");
        if (!Files.exists(parentPath)) {
            try {
                Files.createDirectories(parentPath);
            } catch (IOException ex) {
                throw new CacheClientException(MsgCode.E0004_FILE_CREATE_ERROR, "Could not create directory " + parentPath);
            }
        }
        try {
            return parentPath.toRealPath().toString();
        } catch (IOException ex) {
            throw new CacheClientException(MsgCode.E0004_FILE_CREATE_ERROR, "Could not get absolute path for directory " + parentPath);
        }
    }

    public static class DiscoveryConfigurationBuilder {

        IgniteClientDiscoveryConfiguration instance = new IgniteClientDiscoveryConfiguration();

        public DiscoveryConfigurationBuilder withClientDiscoveryType(ClientDiscoveryType type) {
            instance.discoveryType = type;
            return this;
        }

        public DiscoveryConfigurationBuilder withK8sServiceName(String k8sServiceAccount) {
            instance.k8sServiceAccount = k8sServiceAccount;
            return this;
        }

        public DiscoveryConfigurationBuilder withK8sNameSpace(String nameSpace) {
            instance.nameSpace = nameSpace;
            return this;
        }

        public DiscoveryConfigurationBuilder withIPAddresses(List<String> ipAddresses) {
            instance.ipAddresses = ipAddresses;
            return this;
        }

        public IgniteClientDiscoveryConfiguration build() throws CacheClientException {
            return instance.validate();
        }
    }
}
