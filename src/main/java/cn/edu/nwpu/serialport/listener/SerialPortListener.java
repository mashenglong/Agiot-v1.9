package cn.edu.nwpu.serialport.listener;

import cn.edu.nwpu.utils.ShowUtil;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public  class SerialPortListener implements SerialPortEventListener {

    private DataAvailableListenerInter mDataAvailableListener;

    public SerialPortListener(DataAvailableListenerInter mDataAvailableListener) {
        this.mDataAvailableListener = mDataAvailableListener;
    }
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {
            // 1.There is valid data on the serial port
            case SerialPortEvent.DATA_AVAILABLE:
                if (mDataAvailableListener != null) {
                    mDataAvailableListener.dataAvailable();
                }
                break;
            // 2.The output buffer has been emptied
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            // 3.Clear pending data
            case SerialPortEvent.CTS:
                break;
            // 4.Data to be sent is ready
            case SerialPortEvent.DSR:
                break;
            // 5.Ringing indication
            case SerialPortEvent.RI:
                break;
            // 6. Carrier Detect
            case SerialPortEvent.CD:
                break;
            // 7. Overflow (overflow) error
            case SerialPortEvent.OE:
                break;
            // 8. Parity error
            case SerialPortEvent.PE:
                break;

            // 9. Frame error
            case SerialPortEvent.FE:
                break;
            // 10. Communication interruption
            case SerialPortEvent.BI:
                ShowUtil.errorMessage("Communication with serial device is interrupted");
                break;
            default:
                break;
        }
    }
}