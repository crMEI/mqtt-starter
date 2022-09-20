package com.quanta.mqtt.autoconfigure;


import com.quanta.mqtt.properties.ConnectionProperties;
import com.quanta.mqtt.properties.MqttProperties;
import com.quanta.mqtt.properties.WillProperties;
import com.quanta.mqtt.publisher.MqttClients;
import com.quanta.mqtt.publisher.MqttPublishers;
import com.quanta.mqtt.subscribe.MqttSubscribers;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: mcr
 * @Date: 2022/09/16
 * @Description:
 */
@Order(1010)
@AutoConfigureAfter(MqttSubscribeProcessor.class)
@ConditionalOnClass(MqttAsyncClient.class)
@ConditionalOnProperty(prefix = "quanta.mqtt", name = "disable", havingValue = "false", matchIfMissing = true)
@EnableConfigurationProperties({MqttProperties.class})
@Configuration
public class MqttAutoConfiguration {


    public MqttAutoConfiguration(MqttProperties properties) throws Exception {
        initProperties(properties);
    }

    private void initProperties(MqttProperties properties) throws Exception {
        List<ConnectionProperties> connectionPropertiesList = new ArrayList<>();
        properties.setClientId(getNotEmpty(ConnectionProperties.DEFAULT_CLIENT,properties.getClientId()));
        if(properties.getClients()==null){
            return;
        }
        connectionPropertiesList.add(properties);
        properties.getClients().forEach((clientId,pro)->{
            pro.setUri(getNotEmpty(properties.getUri(),pro.getUri()));
            pro.setClientId(getNotEmpty(clientId,pro.getClientId()));
            pro.setUserName(getNotEmpty(properties.getUserName(),pro.getUserName()));
            pro.setPassword(getNotEmpty(properties.getPassword(),pro.getPassword()));
            pro.setDefaultPublishQos(getNotEmpty(properties.getDefaultPublishQos(),pro.getDefaultPublishQos()));
            pro.setMaxReconnectDelay(getNotEmpty(properties.getMaxReconnectDelay(),pro.getMaxReconnectDelay()));
            pro.setKeepAliveInterval(getNotEmpty(properties.getKeepAliveInterval(),pro.getKeepAliveInterval()));
            pro.setConnectionTimeout(getNotEmpty(properties.getConnectionTimeout(),pro.getConnectionTimeout()));
            pro.setCleanSession(getNotEmpty(properties.getCleanSession(),pro.getCleanSession()));
            pro.setAutomaticReconnect(getNotEmpty(properties.getAutomaticReconnect(),pro.getAutomaticReconnect()));
            pro.setExecutorServiceTimeout(getNotEmpty(properties.getExecutorServiceTimeout(),pro.getExecutorServiceTimeout()));
            pro.setWill(getNotEmpty(properties.getWill(),pro.getWill()));
            connectionPropertiesList.add(pro);
        });

        checkProperties(connectionPropertiesList);
    }

    private void checkProperties(List<ConnectionProperties> connectionPropertiesList) throws Exception {
        if(connectionPropertiesList==null && connectionPropertiesList.size()==0){
            return;
        }
        Set<String> set = new HashSet<>();
        Set<String> repeat =  connectionPropertiesList.stream().map(x->x.getClientId()).filter(x->!set.add(x)).collect(Collectors.toSet());

        if(repeat.size()>0){
            String repeatString = repeat.stream().collect(Collectors.joining(","));
            throw new Exception(String.format("重复clientId :%s", repeatString));
        }
        connectionPropertiesList.forEach(x->{
            Assert.notNull(x.getUri());
            Assert.notNull(x.getUserName());
            Assert.notNull(x.getPassword());
            Assert.notNull(x.getClientId());
        });

    }

    private <T> T getNotEmpty(T main, T sub){
        if(sub != null && !sub.equals("")){
            return sub;
        }
        return main;
    }


    @Bean
    public MqttPublishers mqttPublishers(MqttProperties properties, MqttSubscribers mqttSubscribers){
        MqttClients mqttClients = new MqttClients();
        mqttClients.init(properties,mqttSubscribers);
        MqttPublishers publishers = new MqttPublishers(mqttClients);
        return publishers;
    }




}
