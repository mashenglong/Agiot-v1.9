package cn.edu.nwpu.serialport.listener;

/**
 * Anonymous inner class implements class monitoring
 */
public interface DataAvailableListenerInter {
    /**
     * There is valid data on the serial port
     */
    void dataAvailable();
}