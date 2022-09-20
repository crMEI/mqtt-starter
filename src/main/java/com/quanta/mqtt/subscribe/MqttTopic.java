package com.quanta.mqtt.subscribe;

import org.springframework.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: mcr
 * @Date: 2022/09/20
 * @Description:
 */
public class MqttTopic {

    private String topic;

    private Integer qos;

    private Boolean isShare;

    private String group;

    public MqttTopic(String topic,Integer qos,Boolean isShare,String group){
        this.topic = topic;
        this.qos = qos;
        this.isShare = isShare;
        this.group = group;
    }


    public String getRealTopic(){
        if(this.isShare){
            if(StringUtils.hasLength(this.group)){
                return "$share/" + this.group + "/" + this.topic;
            }else{
                return "$queue/" + this.topic;
            }
        }
        return this.topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getQos() {
        return qos;
    }

    public void setQos(Integer qos) {
        this.qos = qos;
    }

    public Boolean getShare() {
        return isShare;
    }

    public void setShare(Boolean share) {
        isShare = share;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
