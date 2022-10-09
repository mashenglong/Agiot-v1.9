package cn.edu.nwpu.utils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/**
 * mqtt 工具类
 * @author zr
 * @date
 * @param
 * @return
 */
@Slf4j
public class MqttUtil {
    /**
     * 发布消息 MqttMessage
     * @author zr
     * @date
     * @param
     * @return
     */
    public static void publish(MqttClient client, String topic, String msg){
        //MqttClient client = null;
        MqttTopic mqttTopic;
        try {
            MqttMessage message = new MqttMessage();
            message.setQos(1);  //保证消息能到达一次
            message.setRetained(true);
            //String str ="{\"clieId\":\""+clieId+"\",\"mag\":\""+msg+"\"}";
            String str = msg;
            message.setPayload(str.getBytes());
            //client = mqttConnectionPool.getResource();
            mqttTopic = client.getTopic(topic);
            mqttTopic.publish(message);
            log.info("Message is sent to mqtt successfully! The message content is：{}",message.getPayload());
        } catch (MqttException e) {
            log.error("Message sending failed!");
            log.error("The exception information is：{}",e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                // MqttConnectionPool类中release方法抛出异常
                //mqttConnectionPool.release(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
