package cn.edu.nwpu.AppClient.Listener;

import cn.edu.nwpu.AppClient.Inter.ListenerInter;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import lombok.extern.slf4j.Slf4j;

/*
    设置监听事件，当串口到来了数据，就立马触发事件
    调用 dataAvailable() 方法，将数据存储到queue中
 */
@Slf4j
public class SerialPortListener implements SerialPortEventListener {

    private ListenerInter listenerInter;
    public SerialPortListener(ListenerInter listenerInter) {
        this.listenerInter = listenerInter;
    }
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.getEventType() == serialPortEvent.DATA_AVAILABLE){
                listenerInter.dataAvailable();
        }
    }
}
