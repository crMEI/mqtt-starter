package com.quanta.mqtt.properties;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: mcr
 * @Date: 2022/09/16
 * @Description:
 */
public class ConnectionProperties {

    public final static String DEFAULT_CLIENT = "default_client";

    /**
     * broker地址,默认tcp://localhost:1883
     */
    private String[] uri = {"tcp://localhost:1883"};

    /**
     * 客户端id,必填
     */
    private String clientId;

    /**
     * 用户
     */
    private String userName;

    /**
     * 密码
     */
    private String password;


    /**
     * 是否保留信息
     */
    private boolean retained = false;


    /**
     * qos 默认0
     */
    private Integer defaultPublishQos = 0;

    /**
     * 重链接时间，单位秒 默认60s
     */
    private Integer maxReconnectDelay=60;

    /**
     * 心跳，单位秒，默认60s
     */
    private Integer keepAliveInterval =60;

    /**
     * 链接超时，单位秒，默认30
     */
    private Integer connectionTimeout = 30;

    /**
     * 是否清除会话，默认true
     */
    private Boolean cleanSession = true;

    /**
     * 断开是否重链，默认true
     */
    private Boolean automaticReconnect = true;

    /**
     * 发送超时，默认1s
     */
    private Integer executorServiceTimeout = 1;

    /**
     * 遗愿
     */
    private WillProperties will;


    public String[] getUri() {
        return uri;
    }

    public void setUri(String[] uri) {
        this.uri = uri;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRetained() {
        return retained;
    }

    public void setRetained(boolean retained) {
        this.retained = retained;
    }

    public Integer getDefaultPublishQos() {
        return defaultPublishQos;
    }

    public void setDefaultPublishQos(Integer defaultPublishQos) {
        this.defaultPublishQos = defaultPublishQos;
    }

    public Integer getMaxReconnectDelay() {
        return maxReconnectDelay;
    }

    public void setMaxReconnectDelay(Integer maxReconnectDelay) {
        this.maxReconnectDelay = maxReconnectDelay;
    }

    public Integer getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public void setKeepAliveInterval(Integer keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Boolean getCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(Boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public Boolean getAutomaticReconnect() {
        return automaticReconnect;
    }

    public void setAutomaticReconnect(Boolean automaticReconnect) {
        this.automaticReconnect = automaticReconnect;
    }

    public Integer getExecutorServiceTimeout() {
        return executorServiceTimeout;
    }

    public void setExecutorServiceTimeout(Integer executorServiceTimeout) {
        this.executorServiceTimeout = executorServiceTimeout;
    }

    public WillProperties getWill() {
        return will;
    }

    public void setWill(WillProperties will) {
        this.will = will;
    }
}
