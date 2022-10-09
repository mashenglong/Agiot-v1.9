package cn.edu.nwpu.serialport.Timer;
import cn.edu.nwpu.serialport.manage.SerialPortManager;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.TimerTask;
@Slf4j
public class MyTimerTask extends TimerTask {
    SerialPort serialPort;
    byte[] order;
    int sendIdentification;
    public MyTimerTask(SerialPort serialPort,byte[] order,int sendIdentification) {
        this.serialPort = serialPort;
        this.order = order;
        this.sendIdentification = sendIdentification;
    }
    @Override
    public void run() {
        SerialPortManager.sendToPort(serialPort, order);
        if (sendIdentification == 0) {
            log.info("lora GateWay send request frame"+ Arrays.toString(order) +" to LoRa end-device，over");
        } else if (sendIdentification == 1) {
           // log.info("send  lora msg : "+ Arrays.toString(order) +" to Main");
            System.out.println("send  lora msg : "+ Arrays.toString(order) +" to Main");
        } else if (sendIdentification == 2) {
           // log.info("send msg zigbee :" + Arrays.toString(order) +" to Main");
            System.out.println("send msg zigbee :" + Arrays.toString(order) +" to Main");
        }
    }
}
