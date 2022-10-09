package cn.edu.nwpu.mqtt.pool;


public class AutoMqttProperties {
    // broker连接地址，是否考虑用文件，解耦硬编码
    private String host;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 身份id
    private String clientId;
    // 超时（单位是秒）
    private int timeout;
    // 会话心跳时间 （单位为秒）
    private int keepalive;

    public AutoMqttProperties(String host, String username, String password, String clientId, int timeout, int keepalive) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.timeout = timeout;
        this.keepalive = keepalive;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(int keepalive) {
        this.keepalive = keepalive;
    }
}
