package cn.edu.nwpu.mysql.pojo;
public class Sensor {
    // sensor number
    private int sId;
    // The moment of sensor data acquisition
    private String sDate;
    // Temperature data collected by the sensor
    private double sTemperature;
    // Humidity data collected by the sensor
    private double sHumidity;
    public Sensor() {
        this(1,10,10,"20220109");
    }
    public Sensor(int sId, double sTemperature, double sHumidity, String sDate) {
        this.sId = sId;
        this.sTemperature = sTemperature;
        this.sHumidity = sHumidity;
        this.sDate = sDate;
    }
    public Sensor(int sId, double sTemperature, double sHumidity) {
        this.sId = sId;
        this.sTemperature = sTemperature;
        this.sHumidity = sHumidity;
    }
    public int getsId() {
        return sId;
    }
    public void setsId(int sId) {
        this.sId = sId;
    }
    public String getsDate() {
        return sDate;
    }
    public void setsDate(String sDate) {
        this.sDate = sDate;
    }
    public double getsTemperature() {
        return sTemperature;
    }
    public void setsTemperature(double sTemperature) {
        this.sTemperature = sTemperature;
    }
    public double getsHumidity() {
        return sHumidity;
    }
    public void setsHumidity(double sHumidity) {
        this.sHumidity = sHumidity;
    }
    @Override
    public String toString() {
        return "[sensor: " + sId + ", date:" + sDate + ", temperature:" + sTemperature +
                ", humidity: " + sHumidity + "]";
    }
}
