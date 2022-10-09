package cn.edu.nwpu.serialport.data;
import cn.edu.nwpu.mysql.pojo.Sensor;
import cn.edu.nwpu.utils.ByteUtil;
import cn.edu.nwpu.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Slf4j
public class ParseZigbeeForCC2530  implements  ParseData<Short>{
   @Override
   public Short[] parseData(byte[] bytes) {
       //bytes[3] bytes[16] bytes[17],bytes[28],bytes[29]
       //END1 temperature:11 humidity:11
       log.info("parse zigbee data"+ Arrays.toString(bytes));
       Short[] sensors = new Short[3];
       short deviceId = (short) (bytes[3]  - 0x30);

       short tem = (short) ((bytes[16] - 0x30) *10);
       tem |= (bytes[17] - 0x30);

       short humi = (short)((bytes[28] - 0x30) * 10);
       humi |= (bytes[29] - 0x30);

       sensors[0] = (short)(deviceId + 100);
       sensors[1] = tem;
       sensors[2] = humi;
       return  sensors;
   }

    public static void main(String[] args) {
        byte[] bytes = {69, 78, 68, 49, 32, 116, 101, 109, 112, 101, 114, 116, 117,
                114, 101, 58, 49, 49, 32, 104, 117, 109, 105, 100, 105, 116, 121, 58, 49, 49, 13, 10};
        Short[]  shorts = new ParseZigbeeForCC2530().parseData(bytes);
        System.out.println(Arrays.toString(shorts));
    }
}
