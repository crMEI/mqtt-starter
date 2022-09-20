package com.quanta.mqtt.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: mcr
 * @Date: 2022/09/16
 * @Description:
 */
@ConfigurationProperties(prefix = "quanta.mqtt.will")
public class WillProperties {

    /**
     * 遗愿主题.
     */
    private String topic;
    /**
     * 遗愿消息内容.
     */
    private String payload;
    /**
     * 遗愿消息QOS.
     */
    private Integer qos;
    /**
     * 遗愿消息是否保留.
     */
    private Boolean retained;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Integer getQos() {
        return qos;
    }

    public void setQos(Integer qos) {
        this.qos = qos;
    }

    public Boolean getRetained() {
        return retained;
    }

    public void setRetained(Boolean retained) {
        this.retained = retained;
    }
}
