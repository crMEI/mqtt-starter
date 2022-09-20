package com.quanta.mqtt.publisher;

import com.quanta.mqtt.properties.ConnectionProperties;
import com.quanta.mqtt.properties.WillProperties;
import com.quanta.mqtt.subscribe.MqttSubscriber;
import com.quanta.mqtt.subscribe.MqttTopic;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: mcr
 * @Date: 2022/09/19
 * @Description:
 */
public class MqttClient {

    private final static Logger log = LoggerFactory.getLogger(MqttClient.class);

    private ConnectionProperties connectionProperties;

    private MqttConnectOptions connectOptions;

    private IMqttAsyncClient client;

    private List<MqttSubscriber> subscribers = new ArrayList<>();

    public List<MqttSubscriber> getSubscribers() {
        return subscribers;
    }

    public MqttClient(ConnectionProperties properties){
        this.connectionProperties = properties;
        this.connectOptions = toConnectOptions(properties);
    }

    public MqttClient(ConnectionProperties properties,List<MqttSubscriber> subscribers){
        this.connectionProperties = properties;
        this.connectOptions = toConnectOptions(properties);
        if(subscribers!=null && subscribers.size()>0){
            for (MqttSubscriber subscriber : subscribers) {
                if(subscriber.getClientIds().length==0){
                    this.subscribers.add(subscriber);
                }else{
                   boolean contain = Arrays.stream(subscriber.getClientIds()).collect(Collectors.toSet()).contains(getClientId());
                   if(contain){
                       this.subscribers.add(subscriber);
                   }
                }
            }
        }
    }

    private MqttConnectOptions toConnectOptions(ConnectionProperties properties){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMaxReconnectDelay(properties.getMaxReconnectDelay() * 1000);
        options.setKeepAliveInterval(properties.getKeepAliveInterval());
        options.setConnectionTimeout(properties.getKeepAliveInterval());
        options.setCleanSession(properties.getCleanSession());
        options.setAutomaticReconnect(properties.getAutomaticReconnect());
        options.setExecutorServiceTimeout(properties.getExecutorServiceTimeout());
        options.setServerURIs(properties.getUri());
        if (StringUtils.hasText(properties.getUserName()) && StringUtils.hasText(properties.getPassword())) {
            options.setUserName(properties.getUserName());
            options.setPassword(properties.getPassword().toCharArray());
        }
        if (properties.getWill() != null) {
            WillProperties will = properties.getWill();
            if (StringUtils.hasText(will.getTopic()) && StringUtils.hasText(will.getPayload())) {
                options.setWill(will.getTopic(), will.getPayload().getBytes(StandardCharsets.UTF_8), will.getQos(), will.getRetained());
            }
        }
        return options;
    }

    public int getTopicLength(){
        int len = 0;
        if(this.subscribers==null || this.subscribers.size()==0){
            return len;
        }
        len = subscribers.stream().mapToInt(x -> x.getMqttTopicList().size()).sum();
        return len;
    }

    public String getClientId(){
        return this.connectionProperties.getClientId();
    }

    public IMqttAsyncClient getClient(){
        return this.client;
    }

    public ConnectionProperties getConnectionProperties() {
        return connectionProperties;
    }

    public void messageRxProcess(String topic, MqttMessage mqttMessage){
        getSubscribers().forEach(x->x.messageRx(getClientId(),topic,mqttMessage));
    }


    public void doConnect() throws Exception {
        MqttClient mqttClient = this;
        client = new MqttAsyncClient(this.connectOptions.getServerURIs()[0],this.connectionProperties.getClientId(),new MemoryPersistence());
        IMqttToken connect = client.connect(this.connectOptions, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                log.info("mqtt connect success,clientId is [{}],broker is [{}]",client.getClientId(),client.getServerURI());
                try{
                    subscribe(mqttClient);
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                log.info("mqtt connect fail,clientId is [{}],broker is [{}]",client.getClientId(),client.getServerURI());
                throwable.printStackTrace();

            }
        });


        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if(reconnect){
                    log.info("clientId [{}] reconnect",mqttClient.getClientId());
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                log.error("clientId [{}] connection lost",mqttClient.getClientId());
                try{
//                    client.reconnect();
                }catch (Exception e){

                }

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                mqttClient.messageRxProcess(topic,mqttMessage);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                log.info("clientId [{}] deliveryComplete",mqttClient.getClientId());
            }
        });
    }

    private void subscribe(MqttClient client) throws Exception{

        int topicLen = getTopicLength();
        if(topicLen == 0){
            return;
        }
        List<MqttSubscriber> subscribers = client.getSubscribers();
        int p = 0;
        String[] topicArray = new String[topicLen];
        int[] qosArray = new int[topicLen];

        for (int i = 0; i<subscribers.size() ; i++) {
            MqttSubscriber subscriber = subscribers.get(i);
            for (MqttTopic mqttTopic : subscriber.getMqttTopicList()) {
                topicArray[p]= mqttTopic.getRealTopic();
                qosArray[p] = mqttTopic.getQos();
                p++;
            }
        }
        client.client.subscribe(topicArray,qosArray);
        String topicString = String.join(",",topicArray);
        String qosString = Arrays.stream(qosArray).mapToObj(x->x+"").collect(Collectors.joining(","));
        log.info("client [{}] subscribe topics[{}], qos[{}]",client.getClientId(),topicString,qosString);
    }



}
