package com.quanta.mqtt.subscribe;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: mcr
 * @Date: 2022/09/19
 * @Description:
 */
public final class MqttSubscribeMsg {
    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 主题
     */
    private String topic;

    /**
     * 消息主题
     */
    private MqttMessage message;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public MqttMessage getMessage() {
        return message;
    }

    public void setMessage(MqttMessage message) {
        this.message = message;
    }

    public String getPayload(){
        return new String(this.message.getPayload());
    }
}
