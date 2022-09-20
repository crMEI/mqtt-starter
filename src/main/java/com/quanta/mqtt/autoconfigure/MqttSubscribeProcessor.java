package com.quanta.mqtt.autoconfigure;

import com.quanta.mqtt.annotion.MqttSubscribe;
import com.quanta.mqtt.properties.MqttProperties;
import com.quanta.mqtt.properties.WillProperties;
import com.quanta.mqtt.subscribe.MqttSubscribers;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.lang.reflect.Method;

import static java.lang.Boolean.TRUE;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: mcr
 * @Date: 2022/09/19
 * @Description:
 */
@AutoConfigureAfter(MqttSubscribers.class)
@ConditionalOnProperty(prefix = "quanta.mqtt", name = "disable", havingValue = "false", matchIfMissing = true)
@EnableConfigurationProperties({MqttProperties.class})
@Configuration
public class MqttSubscribeProcessor implements BeanPostProcessor {

    @Autowired
    MqttProperties mqttProperties;

    @Autowired
    MqttSubscribers mqttSubscribers;

    public MqttSubscribeProcessor(){
        System.out.println("MqttSubscribeProcessor()");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(TRUE.equals( mqttProperties.getDisable())){
            return bean;
        }
        Method[] methods = bean.getClass().getMethods();
        for(Method method:methods){
            if(method.isAnnotationPresent(MqttSubscribe.class)){
                mqttSubscribers.init(bean,method);
            }
        }
        return bean;
    }

    @Bean
    MqttSubscribers subscribers(){
        return new MqttSubscribers();
    }


}
