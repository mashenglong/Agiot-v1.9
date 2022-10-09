package cn.edu.nwpu.serialport.manage;
import cn.edu.nwpu.serialport.listener.DataAvailableListenerInter;
import cn.edu.nwpu.serialport.listener.SerialPortListener;
import cn.edu.nwpu.serialport.queue.LinkedListQueue;
import gnu.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * This class belongs to the general class for managing serial ports,
 * realizes finding serial ports, opening specified serial ports, closing
 * specified serial ports, opening list serial ports, closing list serial ports,
 * Read data from the specified serial port, write data to the specified serial port,
 * set the registration monitoring event for the specified serial port,
 */

public class SerialPortManager {
    /**
     * Find the serial port under the current system
     * @return Returns all serial ports under the current system
     */
    public static ArrayList<String> findPorts() {
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        ArrayList<String> portNameList = new ArrayList<String>();
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
            portNameList.add(portName);
        }
        return portNameList;
    }
    /**
     * Enter the serial port name and baud rate to open the specified serial port
     * @param portName
     * @param baudRate
     * @return Returns the open serial port object
     */
    public static final SerialPort openPort(String portName, int baudRate) throws PortInUseException {
          try {
            // 通过端口名识别端口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            CommPort commPort = portIdentifier.open(portName, 2000);
            //判断是不是串口
          if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                try {
                    // 设置一下串口的波特率等参数
                    // 数据位：8
                    // 停止位：1
                    // 校验位：None
                    serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                } catch (Exception e) {

                }
                return serialPort;
            }
        } catch (NoSuchPortException e1) {
            e1.printStackTrace();
        }
        return null;
    }
    /**
     * Close the serial port in the serial port list
     * @param portNameList serial port list
     * @param baudRateList baud Rate list
     * @return Returns the open serial port object list
     */
    public static final List<SerialPort> openPortList(List<String> portNameList, List<Integer> baudRateList) throws PortInUseException {
        List<SerialPort> lSerialPort = new ArrayList<>();
        try {
            for(int i = 0 ; i < portNameList.size() ; i ++) {
                String portName = portNameList.get(i);
                int pBaudRate = baudRateList.get(i);
                CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
                CommPort commPort = portIdentifier.open(portName, 2000);
                if (commPort instanceof SerialPort) {
                    SerialPort serialPort = (SerialPort) commPort;
                    serialPort.setSerialPortParams(pBaudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                                                    SerialPort.PARITY_NONE);
                    lSerialPort.add(serialPort);
                }
            }
            return lSerialPort;
            } catch (UnsupportedCommOperationException e) {
                    e.printStackTrace();
            } catch (NoSuchPortException e1) {
                    e1.printStackTrace();
            } catch (Exception e2) {
                    e2.printStackTrace();
            }
        return null;
    }
    /**
     * Close the specified serial port
     * @param serialPort
     */
    public static void closePort(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.close();
        }
    }
    /**
     * Close the specified serial port list
     * @param lSerialPorts
     */
    public static void closePortList(List<SerialPort> lSerialPorts) {
        for(SerialPort serialPort : lSerialPorts) {
            if (serialPort != null) {
                serialPort.close();
            }
        }
    }
    /**
     * Send data to serial port
     * @param serialPort
     * @param data data to be sent
     */
    public static void sendToPort(SerialPort serialPort, byte[] data) {
        OutputStream out = null;
        try {
            out = serialPort.getOutputStream();
            out.write(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    out = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Read data from serial port
     * @param serialPort SerialPort object of the currently established connection
     * @param linkedListQueue empty queue
     * @return Store data read from serial port
     */
    public static LinkedListQueue readFromPort(SerialPort serialPort, LinkedListQueue linkedListQueue) {
        InputStream in = null;
        try {
            in = serialPort.getInputStream();
            byte[] readBuffer = new byte[1];
            while(in.available() > 0) {
                //从in中读取一个readBuffer大小的数据的ASCII码，放在readBuffer中
                in.read(readBuffer);
                //将读取的一个字节数据的ASCII码存储到队列中
                linkedListQueue.enqueue(readBuffer[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return linkedListQueue;
    }
    /**
     * Add Listener to serial port
     * @param  serialPort The serial port object that needs to be set to Listener
     * @param  listener
     */
    public static void addListener(SerialPort serialPort, DataAvailableListenerInter listener) {
        try {
            serialPort.addEventListener(new SerialPortListener(listener));
            // 设置当有数据到达时唤醒监听接收线程
            serialPort.notifyOnDataAvailable(true);
            // 设置当通信中断时唤醒中断线程
            serialPort.notifyOnBreakInterrupt(true);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }
}

