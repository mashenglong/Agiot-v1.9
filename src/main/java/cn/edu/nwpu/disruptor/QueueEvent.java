package cn.edu.nwpu.disruptor;
import cn.edu.nwpu.mysql.pojo.Sensor;
public class QueueEvent {
    private Sensor sensorData;
    public Sensor getSensorData() {
        return sensorData;
    }
    public void setSensorData(Sensor sensorData) {
        this.sensorData = sensorData;
    }
    void clear()
    {
        sensorData = null;
    }
    @Override
    public String toString() {
        return "QueueEvent{" +
                "sensorData=" + sensorData +
                '}';
    }
}
