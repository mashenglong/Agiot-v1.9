package cn.edu.nwpu.disruptor;
import cn.edu.nwpu.utils.MqttUtil;
import cn.edu.nwpu.mqtt.pool.MqttConnectionPool;
import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
@Slf4j
public class MQTTEventHandler implements WorkHandler<QueueEvent>, EventHandler<QueueEvent> {
    private Integer maxActive = 10;
    private Long maxWait = 10000000L;
    private MqttConnectionPool mPool = new MqttConnectionPool(maxActive, maxWait);
    public MQTTEventHandler() {
    }
    /**
     * When there is a message, onEvent is based on the event monitoring model, which can be monitored.
     */
    @Override
    public void onEvent(QueueEvent queueEvent, long l, boolean b) throws Exception {
        this.onEvent(queueEvent);
    }
    @Override
    public void onEvent(QueueEvent queueEvent) throws Exception {
        Integer maxActive = 10;
        Long maxWait = 10000000L;
        MqttClient mqttClient = mPool.getResource();
        MqttUtil.publish(mqttClient, "test", JSON.toJSONString(queueEvent.getSensorData()));
        mPool.release(mqttClient);
        log.info("Send data to MQTT server, data is " + queueEvent.getSensorData().toString());
    }
}


