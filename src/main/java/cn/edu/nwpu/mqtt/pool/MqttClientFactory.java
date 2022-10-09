package cn.edu.nwpu.mqtt.pool;
import cn.edu.nwpu.mqtt.client.PushCallback;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
/**
 * MqttClientFactory的实现类
 */
@Slf4j
public class MqttClientFactory implements MqttClientFactoryInter {
    /**
     * 生产 MqttClient
     * @return
     */
    @Override
    public MqttClient createMqttClient(AutoMqttProperties autoMqttProperties) {
        if(autoMqttProperties == null){
        //    log.info("MqttClientFactory mqtt连接的配置信息为：{}","null");
            return null;
        }
     //   log.info("MqttClientFactory mqtt连接的配置信息：{}",autoMqttProperties.toString());
        // 得到连接
        MqttClient client = null;
        try {
            // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            // System.currentTimeMillis()
            client = new MqttClient(autoMqttProperties.getHost(),
                            autoMqttProperties.getClientId()+"_"+ UUID.getUUID(),
                            new MemoryPersistence());
        } catch (MqttException e) {
         //   log.error("MqttClientFactory 与mqtt代理连接失败！");
         //   log.error("MqttClientFactory 异常信息为：{}",e.getMessage());
        }
        // MQTT的连接设置
        MqttConnectOptions options = new MqttConnectOptions();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        options.setCleanSession(false);
        // 设置连接的用户名
        options.setUserName(autoMqttProperties.getUsername());
        // 设置连接的密码
        options.setPassword(autoMqttProperties.getPassword().toCharArray());
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(autoMqttProperties.getTimeout());
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(autoMqttProperties.getKeepalive());
        try {
            // 回调
            client.setCallback(new PushCallback());
            // 连接
            client.connect(options);
        } catch (MqttException e) {
            log.error("MqttClientFactory 创建连接失败！");
            log.error("MqttClientFactory 异常信息为：{}",e.getMessage());
        }
        return client;
    }
}
