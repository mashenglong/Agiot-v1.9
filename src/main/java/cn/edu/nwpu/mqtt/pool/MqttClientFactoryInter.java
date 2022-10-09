package cn.edu.nwpu.mqtt.pool;
import org.eclipse.paho.client.mqttv3.MqttClient;
/**
 * mqtt 连接工厂类
 */
public interface MqttClientFactoryInter {
    // 生产 MqttClient
    MqttClient createMqttClient(AutoMqttProperties autoMqttProperties);
}
