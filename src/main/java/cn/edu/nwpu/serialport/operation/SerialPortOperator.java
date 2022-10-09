package cn.edu.nwpu.serialport.operation;

import cn.edu.nwpu.utils.PropertiesUtil;
import cn.edu.nwpu.utils.ShowUtil;
import cn.edu.nwpu.disruptor.QueueHelper;
import cn.edu.nwpu.mysql.pojo.Sensor;
import cn.edu.nwpu.serialport.listener.DataAvailableListenerInter;
import cn.edu.nwpu.serialport.manage.SerialPortManager;
import cn.edu.nwpu.serialport.queue.LinkedListQueue;
import cn.edu.nwpu.serialport.data.ParseData;
import cn.edu.nwpu.serialport.data.ParseLoRaData;
import cn.edu.nwpu.serialport.data.ParseZigbeeForCC2530;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class SerialPortOperator {
    private SerialPort mSerialport;
    private QueueHelper queueHelper;
    /*
        使用多态实现多协议数据解析
     */
    private ParseData parseData;
    private LinkedListQueue queue = new LinkedListQueue();
    public SerialPortOperator() {
    }
    public SerialPortOperator(QueueHelper queueHelper, int deviceType, SerialPort mSerialPort) {
        this.queueHelper = queueHelper;
        this.mSerialport = mSerialPort;
        if(deviceType == 1){
            parseData = new ParseLoRaData();
        }
        else if(deviceType == 2) {
            parseData = new ParseZigbeeForCC2530();
        }
    }
    public void addSerialPortListener(String commName) {
        if (mSerialport != null) {
            log.info("SerialPort "+ commName+ " is opening");
        }
        /**
         *  Use anonymous inner classes, in order to be able to make inner
         *  classes to use member variables of outer classes
         */
        SerialPortManager.addListener(mSerialport, new DataAvailableListenerInter() {
            @Override
            public void dataAvailable() {
                byte[] data = null;
                String serialNamePrefix = "";
                PropertiesUtil com = new PropertiesUtil("com");
                //Use it to distinguish whether it is running on windows or Ubuntu
                if (System.getProperty("os.name").contains("Windows")) {
                    serialNamePrefix = "//./";
                }
                try {
                    if (mSerialport == null) {
                        ShowUtil.errorMessage("The serial port object is empty, monitoring failed!");
                    } else if (mSerialport.getName().equals(serialNamePrefix + com.getUrlValue("LoraCom"))) {
                        log.info("GateWay get the data of the lora end-device and start parsing");
                        queue = SerialPortManager.readFromPort(mSerialport,queue);
                        while (queue.getSize() >= 17) {
                            data = queue.dequeueNEle(17);
                            Short[] sensors = (Short[]) parseData.parseData(data);
                            double tem = sensors[0] / 10.0, hum = sensors[1] / 10.0;
                            Date d = new Date();
                            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            String sDate = ft.format(d);
                            Sensor lora_sensor = new Sensor(1, tem, hum, sDate);
                            log.info("lora sensor data:" + lora_sensor.toString());
                            queueHelper.SendMessageToHandler(lora_sensor);
                        }
                    } else if (mSerialport.getName().equals(serialNamePrefix + com.getUrlValue("ZigbeeCom"))) {
                        log.info("GateWay get the data of the zigbee end-device and start parsing");
                        queue = SerialPortManager.readFromPort(mSerialport,queue);
                        while (queue.getSize() >= 32) {
                            data = queue.dequeueNEle_forZigbee(32);
                            Short[] zigbArr= (Short[]) parseData.parseData(data);
                            Date d = new Date();
                            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            String sDate = ft.format(d);
                            Sensor zigSensor = new Sensor(zigbArr[0],zigbArr[1],zigbArr[2],sDate);
                            log.info("zigbee sensor data:" + zigSensor.toString());
                            queueHelper.SendMessageToHandler(zigSensor);
                        }
                    }
                } catch (Exception e) {
                    log.info("reading serial port error  ");
                    e.printStackTrace();
                }
            }
        });
    }
    public static void closeSerialPort(SerialPort mSerialport) {
        SerialPortManager.closePort(mSerialport);
        log.info("Serial port is closed" + "\r\n");
        mSerialport = null;
    }
}