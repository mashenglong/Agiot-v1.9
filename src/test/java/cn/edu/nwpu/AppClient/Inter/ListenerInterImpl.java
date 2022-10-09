package cn.edu.nwpu.AppClient.Inter;

import cn.edu.nwpu.AppClient.Inter.ListenerInter;
import cn.edu.nwpu.serialport.queue.LinkedListQueue;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ListenerInterImpl implements ListenerInter {
    private SerialPort serialPort;
    private LinkedListQueue linkedListQueue;
    public ListenerInterImpl(SerialPort serialPort, LinkedListQueue linkedListQueue) {
        this.serialPort = serialPort;
        this.linkedListQueue = linkedListQueue;
    }
    @Override
    public void dataAvailable() {
        InputStream in;
        if ( serialPort.getName().equals("//./COM2")){
            try {
                in = serialPort.getInputStream();
                byte[] readBuffer = new byte[1];
                if (in.available() > 0) {
                    //从in中读取一个readBuffer大小的数据，放在readBuffer中
                    in.read(readBuffer);
                    linkedListQueue.enqueue(readBuffer[0]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if ( serialPort.getName().equals("//./COM6")){
            try {
                in = serialPort.getInputStream();
                byte[] readBuffer = new byte[1];
                if (in.available() > 0) {
                    //从in中读取一个readBuffer大小的数据，放在readBuffer中
                    in.read(readBuffer);
                    linkedListQueue.enqueue(readBuffer[0]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
