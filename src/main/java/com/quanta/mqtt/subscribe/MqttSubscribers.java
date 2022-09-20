package com.quanta.mqtt.subscribe;


import com.quanta.mqtt.annotion.MqttSubscribe;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: mcr
 * @Date: 2022/09/19
 * @Description:
 */
public class MqttSubscribers {

    public static List<MqttSubscriber> SUBSCRIBER_LIST = new ArrayList<>();

    public MqttSubscribers(){
        System.out.println("subscribe init");
    }

    public void init(Object bean, Method method){
        if (method.isAnnotationPresent(MqttSubscribe.class)) {
            Parameter[] parameters = method.getParameters();
            if(parameters.length !=1){
                return;
            }
            if(parameters[0].getType().isAssignableFrom(MqttSubscribeMsg.class)){
                SUBSCRIBER_LIST.add(MqttSubscriber.of(bean,method));
            }
        }
    }

    public List getSubscribers(){
        return SUBSCRIBER_LIST;
    }




}
