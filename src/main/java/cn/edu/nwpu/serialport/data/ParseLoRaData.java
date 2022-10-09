package cn.edu.nwpu.serialport.data;

import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;

@Slf4j
public class ParseLoRaData implements ParseData<Short> {
    @Override
    public Short[] parseData(byte[] bytes) {
        short temperature, humidity;
        log.info("parse lora data"+ Arrays.toString(bytes));
        Short[] sensors = new Short[2];
        // Take out humidity
        int iHum = (bytes[10] & 0xFF);
        iHum |= (bytes[11] & 0xFF) << 8;
        humidity = (short) iHum;
        // Take out temperature
        int iTem = (bytes[12] & 0xFF);
        iTem |= (bytes[13] & 0xFF) << 8;
        temperature = (short) iTem;
        // 存放到数组中
        sensors[0] = temperature;
        sensors[1] = humidity;
        return sensors;
    }
}
