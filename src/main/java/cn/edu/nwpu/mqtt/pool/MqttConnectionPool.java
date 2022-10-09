package cn.edu.nwpu.mqtt.pool;
import cn.edu.nwpu.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zr
 * @date 2020-12-31-20:50
 * @description
 **/
@Slf4j
public class MqttConnectionPool implements ConnectionPoolInter<MqttClient>{
    // 最大活动连接数
    private Integer maxActive;
    // 最大等待时间
    private Long maxWait;
    //空闲队列
    private LinkedBlockingQueue<MqttClient> idle = new LinkedBlockingQueue<>();
    // 繁忙队列
    private LinkedBlockingQueue<MqttClient> busy = new LinkedBlockingQueue<>();
    // 连接池活动连接数
    private AtomicInteger activeSize = new AtomicInteger();
    // 连接池关闭标记
    private AtomicBoolean isClosed = new AtomicBoolean(false);
    //总共获取的连接计数
    private AtomicInteger createCount = new AtomicInteger(0);
    // 用于产生MqttClient对象的MqttClientFactory
    private MqttClientFactory mqttClientFactory = new MqttClientFactory();

    // MQTT连接池
    private static MqttConnectionPool MQTTCONNECTIONPOOL = new MqttConnectionPool();

    public MqttConnectionPool() {}

    public static MqttConnectionPool getInstance() {
        return MQTTCONNECTIONPOOL;
    }

    public MqttConnectionPool(Integer maxActive, Long maxWait) {
        this.init(maxActive, maxWait);
    }
    @Override
    public void init(Integer maxActive, Long maxWait) {
        this.maxActive = maxActive;
        this.maxWait = maxWait;
    }

    @Override
    public MqttClient getResource() throws Exception {
        MqttClient mqttClient;
        Long nowTime = System.currentTimeMillis();
        //空闲队列idle是否有连接
        if((mqttClient = idle.poll()) == null) {
            // 判断池中连接数是否小于maxActive
            if (activeSize.get() < maxActive) {
                // 先增加池中连接数后判断是否小于等于maxActive
                if (activeSize.incrementAndGet() <= maxActive) {
                    // 创建MqttProperty对象
                    PropertiesUtil mqtt = new PropertiesUtil("mqtt");
                    AutoMqttProperties properties =
                            new AutoMqttProperties(
                                    mqtt.getUrlValue("mqtt.host"),
                                    mqtt.getUrlValue("mqtt.username"),
                                    mqtt.getUrlValue("mqtt.password"),
                                    mqtt.getUrlValue("mqtt.clientId"),
                                    Integer.parseInt(mqtt.getUrlValue("mqtt.timeout")),
                                    Integer.parseInt(mqtt.getUrlValue("mqtt.keepalive"))
                            );
                    // 创建MqttClient
                    mqttClient = mqttClientFactory.createMqttClient(properties);
                //    log.info("MqttConnectionPool Thread:" + Thread.currentThread().getId() + "获取连接:" + createCount.incrementAndGet() + "条");
                    busy.offer(mqttClient);
                    return mqttClient;
                } else {
                    // 如增加后发现大于maxActive则减去增加的
                    activeSize.decrementAndGet();
                }
            }
            // 若活动线程已满则等待busy队列释放连接
            try {
                log.info("Thread:" + Thread.currentThread().getId() + "等待获取空闲资源");
                Long waitTime = maxWait - (System.currentTimeMillis() - nowTime);
                mqttClient = idle.poll(waitTime, TimeUnit.MICROSECONDS);
            } catch (Exception e) {
                throw new Exception("等待异常");
            }

            // 判断是否超时
            if (mqttClient != null) {
                log.info("Thread: " + Thread.currentThread().getId() + "获取连接:" + createCount);
                busy.offer(mqttClient);
                return mqttClient;
            } else {
                System.out.println("Thread:" + Thread.currentThread().getId() + "获取连接超时，请重试!");
                throw new Exception("Thread:" + Thread.currentThread().getId() + "获取连接超时，请重试!");
            }
        }
        // 空闲队列有连接，直接返回
        busy.offer(mqttClient);
        return mqttClient;
    }

    // 释放Mqtt连接
    @Override
    public void release(MqttClient connection) throws Exception {
        if(connection == null) {
            log.info("connection为空");
            return;
        }
        if(busy.remove(connection)) {
            idle.offer(connection);
        } else {
            activeSize.decrementAndGet();
            throw new Exception("释放失败");
        }
    }

    // 关闭Mqtt连接池
    @Override
    public void close() {
        if(isClosed.compareAndSet(false, true)) {
            idle.forEach((mqttClient) -> {
                try {
                    mqttClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            busy.forEach((mqttClient -> {
                try {
                    mqttClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }
    }
}
