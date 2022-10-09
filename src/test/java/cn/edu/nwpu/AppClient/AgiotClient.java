package cn.edu.nwpu.AppClient;

import cn.edu.nwpu.AppClient.Inter.ListenerInterImpl;
import cn.edu.nwpu.AppClient.Listener.SerialPortListener;
import cn.edu.nwpu.serialport.Timer.MyTimerTask;
import cn.edu.nwpu.serialport.manage.SerialPortManager;
import cn.edu.nwpu.serialport.queue.LinkedListQueue;
import cn.edu.nwpu.utils.ByteUtil;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TooManyListenersException;
/**
 * For testing virtual serial port for communication
 * When there is no serial port, use this class to simulate
 * the serial port test, and the operation must be after the main method of the Main class
 * Otherwise, the serial port will be occupied, the main method of the Main class will error
 */

@Slf4j
public class AgiotClient {
    ArrayList<SerialPort> serialPortsList = new ArrayList<>();
    public AgiotClient()  {
        CommPortIdentifier portIdentifier = null;
        CommPort commPortNew = null;
        try {
            //COM2 与 lora 通信COM
            portIdentifier = CommPortIdentifier.getPortIdentifier("COM2");
            commPortNew = portIdentifier.open("COM2", 2000);
            serialPortsList.add((SerialPort)commPortNew);
            //COM6 与 zigbee 通信COM
            portIdentifier = CommPortIdentifier.getPortIdentifier("COM6");
            commPortNew = portIdentifier.open("COM6", 2000);
            serialPortsList.add((SerialPort)commPortNew);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addListener(SerialPort serialPort, SerialPortListener serialPortListener) throws TooManyListenersException {
                try {
                    serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                serialPort.addEventListener(serialPortListener);
                // 设置当有数据到达时唤醒监听接收线程
                serialPort.notifyOnDataAvailable(true);
                // 设置当通信中断时唤醒中断线程
                serialPort.notifyOnBreakInterrupt(true);
    }

    public static void main(String[] args) throws TooManyListenersException {

        LinkedListQueue linkedListQueue = new LinkedListQueue();
        AgiotClient agiotClient = new AgiotClient();
        ArrayList<SerialPort> serialPorts = agiotClient.serialPortsList;
        SerialPortListener serialPortListener = new SerialPortListener(new ListenerInterImpl(serialPorts.get(0), linkedListQueue));
        agiotClient.addListener(serialPorts.get(0),serialPortListener);

        //[-8, 0, 0, 0, 8, 0, 112, 55, 2, 0, -110, 2, -101, -1, 53, -34, -24]
        String loraMsg = "F80000000800703702009202010035DEE8";
        byte[] bLoraMsg = ByteUtil.hexStr2Byte(loraMsg);
        //ByteUtil.hexStr2Byte: [-8, 0, 0, 0, 8, 0, 112, 55, 2, 0, -110, 2, 1, 0, 53, -34, -24]
       // byte[] bLoraMsg = loraMsg.getBytes();
        SerialPort loraSerialPort = serialPorts.get(0);
        MyTimerTask myLoraTimerTask = new MyTimerTask(loraSerialPort,bLoraMsg,1);
        Timer loraTimer = new Timer();
        loraTimer.schedule(myLoraTimerTask,1000,10000);

        String zigbeeMsg = "454E44312074656D706572747572653A31312068756D69646974793A31310D0A";
        byte[] bMsg = ByteUtil.hexStr2Byte(zigbeeMsg);
       // byte[] bMsg = zigbeeMsg.getBytes();
        SerialPort zigbeeSerialPort = serialPorts.get(1);
        MyTimerTask myZigbeeTimerTask = new MyTimerTask(zigbeeSerialPort,bMsg,2);
        Timer zigbeeTimer = new Timer();
        zigbeeTimer.schedule(myZigbeeTimerTask,1000,10000);
    }
}
