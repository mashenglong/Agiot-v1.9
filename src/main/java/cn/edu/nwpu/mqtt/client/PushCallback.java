package cn.edu.nwpu.mqtt.client;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
@Slf4j
public class PushCallback implements MqttCallback {
    public void connectionLost(Throwable cause) {
        log.error("PushCallback Thread-"+Thread.currentThread().getId()+": Disconnected, retrying connection。");
    }
    @Override
    public void messageArrived( String topic, MqttMessage mqttMessage ) throws Exception {
        // subscribe后得到的消息会执行到这里面
        log.info("PushCallback messageArrived Receive message subject:{}",topic);
        log.info("PushCallback messageArrived Receive message Qos:{}",mqttMessage.getQos());
        log.info("PushCallback messageArrived Receive message content:{}",new String(mqttMessage.getPayload()));
    }
    public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        log.info("PushCallback messageArrived Receive message subject:{}",topic.getName());
        log.info("PushCallback messageArrived Receive message Qos:{}",message.getQos());
        log.info("PushCallback messageArrived Receive message content:{}",new String(message.getPayload()));
    }
    // When mqtt calls publish, the method will be called
    @Override
    public void deliveryComplete( IMqttDeliveryToken iMqttDeliveryToken ) {
        log.info("delivery data to mqtt Complete: {}",iMqttDeliveryToken.isComplete());
    }

}
