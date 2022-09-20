package com.quanta.mqtt.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: mcr
 * @Date: 2022/09/19
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MqttSubscribe {

    /**
     * 客户端id，不指定则全部客户端订阅
     * @return
     */
    String[] clients() default {};

    /**
     * 订阅主题
     * @return
     */
    String[] topics();

    /**
     * qos
     * @return
     */
    int[] qos() default {0};


    /**
     * 是否共享
     * @return
     */
    boolean[] share() default false;


    /**
     * 共享组（如果share为true时，为订阅组名）
     * @return
     */
    String[] groups() default "";




}
