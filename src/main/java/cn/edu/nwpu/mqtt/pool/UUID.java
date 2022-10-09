package cn.edu.nwpu.mqtt.pool;
/**
 * @author zr
 * @date 2021-01-01-22:56
 * @description
 **/
public class UUID {

    public static String getUUID() {
        String id = java.util.UUID.randomUUID().toString();
        id = id.replace("-", "");
        return id;
    }

}
