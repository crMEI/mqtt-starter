package com.quanta.mqtt.properties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: mcr
 * @Date: 2022/09/16
 * @Description:
 */
@ConfigurationProperties(prefix = "quanta.mqtt")
public class MqttProperties extends ConnectionProperties{





    /**
     * 是否禁止mqtt
     */
    private Boolean disable = false;

    /**
     * 多客户端
     */
    private Map<String,ConnectionProperties> clients;

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public Map<String, ConnectionProperties> getClients() {
        return clients;
    }

    public void setClients(Map<String, ConnectionProperties> clients) {
        this.clients = clients;
    }

}
