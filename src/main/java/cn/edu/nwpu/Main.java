package cn.edu.nwpu;
import cn.edu.nwpu.serialport.Timer.MyTimerTask;
import cn.edu.nwpu.utils.ByteUtil;
import cn.edu.nwpu.utils.PropertiesUtil;
import cn.edu.nwpu.disruptor.QueueHelper;
import cn.edu.nwpu.serialport.manage.SerialPortManager;
import cn.edu.nwpu.serialport.operation.SerialPortOperator;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Startup class, when the main class is running, open the serial ports
 * corresponding to lora and zigbee respectively, and register monitoring
 * events for the corresponding serial ports. If lora wants to get data, it
 * needs to send the inquiry frame regularly,set the inquiry once every 10s
 */
@Slf4j
public class Main {

    final static int DEVICE_TYPE_LORA = 1;
    final static int DEVICE_TYPE_ZIGBEE = 2;

    public static void main(String[] args) {

        PropertiesUtil com = new PropertiesUtil("com");
        SerialPortOperator loRaSPortOp;
        SerialPortOperator zigBeeSPortOp ;
        SerialPort zigBeeSerialPort;
        SerialPort loRaSerialPort = null;
        String zigBeePortName = com.getUrlValue("ZigbeeCom");
        String   loRaPortName = com.getUrlValue("LoraCom");
        int zigBeeBaudRate = Integer.parseInt(com.getUrlValue("ZigbeeBaudRate"));
        int loRaBaudRate = Integer.parseInt(com.getUrlValue("LoraBaudRate"));
        QueueHelper queueHelper;
        String orderStr = "f80000000400703702005fc4";
        byte[] order = ByteUtil.hexStr2Byte(orderStr);
        /*Prepare disruptor and bind consumers to disruptor*/
        queueHelper = new QueueHelper();
        queueHelper.start();

            /* open zigbee serialPort , and add addListener for it */
        try {
            zigBeeSerialPort = SerialPortManager.openPort(zigBeePortName, zigBeeBaudRate);
            zigBeeSPortOp = new SerialPortOperator(queueHelper, DEVICE_TYPE_ZIGBEE, zigBeeSerialPort);
            zigBeeSPortOp.addSerialPortListener(zigBeePortName);
        } catch (PortInUseException e) {
            e.printStackTrace();
        }


            /* open Lora serialPort , and add addListener for it */
        try {
            loRaSerialPort = SerialPortManager.openPort(loRaPortName, loRaBaudRate);
            loRaSPortOp = new SerialPortOperator(queueHelper, DEVICE_TYPE_LORA, loRaSerialPort);
            loRaSPortOp.addSerialPortListener(loRaPortName);
        } catch (PortInUseException e) {
            e.printStackTrace();
        }

        log.info("lora GateWay send request frame to LoRa end-device，start");
        TimerTask task = new MyTimerTask(loRaSerialPort,order,0);
        Timer timer = new Timer();
        timer.schedule(task,1000,10000);

    }
}