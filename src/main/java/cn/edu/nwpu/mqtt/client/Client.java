package cn.edu.nwpu.mqtt.client;
import cn.edu.nwpu.utils.PropertiesUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Use the Client class to see if the message uploaded by
 * the gateway to the mqtt middleware can be obtained accordingto the published topic
 */
public class Client {

    public  String HOST;
    public  String TOPIC;
    private String clientId;
    private MqttClient client;
    private MqttConnectOptions options;
    private String userName;
    private String passWord;

    private ScheduledExecutorService scheduler;

    public void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (!client.isConnected()) {
                    try {
                        client.connect(options);
                    } catch (MqttSecurityException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

    private void start() {
        try {
            PropertiesUtil mqttClient = new PropertiesUtil("mqtt-client");
            client = new MqttClient(mqttClient.getUrlValue("mqtt-client.HOST"),
                    mqttClient.getUrlValue("mqtt-client.TOPIC"),
                    new MemoryPersistence());
            /*Configure the parameters of mqttConnectOption*/
            options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(mqttClient.getUrlValue("mqtt-client.username"));
            options.setPassword(mqttClient.getUrlValue("mqtt-client.password").toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            client.setCallback(new PushCallback());
            MqttTopic topic = client.getTopic(mqttClient.getUrlValue("mqtt-client.TOPIC"));
            options.setWill(topic, "close".getBytes(), 0, true);
            client.connect(options);
            int[] Qos  = {1};
            String[] topic1 = {mqttClient.getUrlValue("mqtt-client.TOPIC")};
            client.subscribe(topic1, Qos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws MqttException {
        Client client = new Client();
        client.start();
    }
}
