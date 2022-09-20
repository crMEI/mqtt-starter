package com.quanta.mqtt.publisher;

import com.quanta.mqtt.properties.ConnectionProperties;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: mcr
 * @Date: 2022/09/20
 * @Description:
 */
public class MqttPublishers {

    private MqttClients mqttClients;

    public MqttPublishers(MqttClients mqttClients){
        this.mqttClients = mqttClients;
    }

    private MqttClient getByClientId(String clientId){
        return mqttClients.getClientMap().get(clientId);
    }

    public void publish(String topic,String payload) throws Exception{
        MqttClient mqttClient = getByClientId(ConnectionProperties.DEFAULT_CLIENT);
        publish(ConnectionProperties.DEFAULT_CLIENT,topic,payload,
                 mqttClient.getConnectionProperties().getDefaultPublishQos(),
                mqttClient.getConnectionProperties().isRetained());
    }

    public void publish(String clientId,String topic,String payload,int qos,boolean retained) throws Exception {
        MqttClient mqttClient = getByClientId(clientId);
        if(mqttClient==null){
            throw new Exception(String.format("clientId [{}] not exist",clientId));
        }
        mqttClient.getClient().publish(topic,buildMessage(payload,qos,retained));
    }

    private MqttMessage buildMessage(String payload,int qos,boolean retained){
        MqttMessage message = new MqttMessage();
        message.setRetained(retained);
        message.setPayload(payload.getBytes());
        message.setQos(qos);
        return message;
    }

}
