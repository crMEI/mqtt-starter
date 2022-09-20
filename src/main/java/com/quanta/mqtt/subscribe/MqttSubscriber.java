package com.quanta.mqtt.subscribe;

import com.quanta.mqtt.annotion.MqttSubscribe;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: mcr
 * @Date: 2022/09/19
 * @Description:
 */
public class MqttSubscriber {

    private Object bean;

    private Method method;

    private String[] clientIds;

    private String[] topics;

    private Integer[] qos;

    private Boolean[] isShare;

    private String[] groups;

    private List<MqttTopic> mqttTopicList;


    public void messageRx(String clientId, String topic,MqttMessage message){
        if(!isMatch(topic)){
            return;
        }
        MqttSubscribeMsg msg = new MqttSubscribeMsg();
        msg.setClientId(clientId);
        msg.setTopic(topic);
        msg.setMessage(message);
        try {
            method.invoke(bean,msg);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private boolean isMatch(String topic){
        for (MqttTopic mqttTopic : this.mqttTopicList) {
            if(org.eclipse.paho.client.mqttv3.MqttTopic.isMatched(mqttTopic.getRealTopic(),topic)){
                return true;
            }
        }
        return false;
    }



    public static MqttSubscriber of(Object bean, Method method){
        MqttSubscriber subscriber = new MqttSubscriber();

        subscriber.bean = bean;
        subscriber.method = method;

        MqttSubscribe subscribe = method.getAnnotation(MqttSubscribe.class);
        subscriber.clientIds = subscribe.clients();
        subscriber.topics = subscribe.topics();
        subscriber.qos = fillQos(subscribe.topics(),subscribe.qos());
        subscriber.isShare = fillIsShare(subscribe.topics(),subscribe.share());
        subscriber.groups = fillGroups(subscribe.topics(),subscribe.groups());
        subscriber.mqttTopicList = getTopicList(subscriber);
        return subscriber;
    }

    private static List<MqttTopic> getTopicList(MqttSubscriber subscriber){
        List<MqttTopic> mqttTopicList = new ArrayList<>();
        int len = subscriber.getTopics().length;
        for(int i = 0;i<len;i++){
            MqttTopic topic = new MqttTopic(subscriber.topics[i],subscriber.qos[i],subscriber.isShare[i],subscriber.groups[i]);
            mqttTopicList.add(topic);
        }
        return mqttTopicList;
    }


    private static Integer[] fillQos(String[] topics, int[] qos){
        Integer[] fill = new Integer[topics.length];
        int p = 0;
        for(;p<qos.length;p++){
            fill[p] = qos[p];
        }
        if(topics.length>qos.length){
            for(;p< topics.length;p++){
                fill[p] = 0;
            }
        }
        return fill;
    }

    private static Boolean[] fillIsShare(String[] topics,boolean[] isShare){
        Boolean[] fill = new Boolean[topics.length];
        int p = 0;
        for(;p<isShare.length;p++){
            fill[p] = isShare[p];
        }
        if(topics.length>isShare.length){
            for(;p<topics.length;p++){
                fill[p] = false;
            }
        }
        return fill;
    }

    private static String[] fillGroups(String[] topics,String[] groups){
        String[] fill = new String[topics.length];
        int p = 0;
        for(;p<groups.length;p++){
            fill[p] = groups[p];
        }
        if(topics.length>groups.length){
            for(;p <topics.length;p++){
                fill[p] = "";
            }
        }
        return fill;
    }


    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String[] getClientIds() {
        return clientIds;
    }

    public void setClientIds(String[] clientIds) {
        this.clientIds = clientIds;
    }

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }

    public Integer[] getQos() {
        return qos;
    }

    public void setQos(Integer[] qos) {
        this.qos = qos;
    }

    public Boolean[] getIsShare() {
        return isShare;
    }

    public void setIsShare(Boolean[] isShare) {
        this.isShare = isShare;
    }

    public String[] getGroups() {
        return groups;
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }

    public List<MqttTopic> getMqttTopicList() {
        return mqttTopicList;
    }

    public void setMqttTopicList(List<MqttTopic> mqttTopicList) {
        this.mqttTopicList = mqttTopicList;
    }
}
