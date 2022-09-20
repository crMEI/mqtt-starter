package com.quanta.mqtt.publisher;

import com.quanta.mqtt.properties.MqttProperties;
import com.quanta.mqtt.subscribe.MqttSubscribers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: mcr
 * @Date: 2022/09/19
 * @Description:
 */
public class MqttClients {

    private static Map<String,MqttClient> clientMap = new HashMap<>();

    public void init(MqttProperties mqttProperties, MqttSubscribers mqttSubscribers){


        clientMap.put(mqttProperties.getClientId(),new MqttClient(mqttProperties,mqttSubscribers.getSubscribers()));

        if(mqttProperties.getClients()!=null && mqttProperties.getClients().size()>0){
            mqttProperties.getClients().forEach((clientId,properties)->{
                clientMap.put(clientId,new MqttClient(properties,mqttSubscribers.getSubscribers()));
            });
        }
        //start connect and subscribe
        clientMap.forEach((k,v)-> {
            try {
                v.doConnect();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }


    public  Map<String, MqttClient> getClientMap() {
        return clientMap;
    }
}
