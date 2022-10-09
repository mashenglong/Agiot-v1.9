package cn.edu.nwpu.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

public class DataUtil {
    //将string类型的传感器数据转换成json格式并返回
    public String stringToJson(String data) {
        HashMap<String, Object> hashMap = JSONObject.parseObject(data, HashMap.class);
        String json = JSON.toJSONString(hashMap);
        return json;
    }

    /**
     * ZigBee 将传入byte数组中的数据解析成传感器数据
     * v2 增加ZigBee设备后添加
     * @param bytes
     * @return
     *
     * 因为 zigbee 有两种节点，一种TinyOS，一种CC2530芯片 所以针对于不同的芯片所使用的解析规则有区别
     */
    public static int[] parseZigBeeData(byte[] bytes) {
        int[] sensor = new int[2];
        int temperature = Integer.parseInt(String.valueOf(bytes[4]), 16);
        int humidity = Integer.parseInt(String.valueOf(bytes[6]), 16);
        sensor[0] = temperature;
        sensor[1] = humidity;
        return sensor;
    }

    /**
     * LoRa 将传入byte数组中的数据解析成温湿度
     * @param bytes 传入的byte数组
     * @return 解析后的温湿度数据数组
     */
    public static short[] parseSensorData(byte[] bytes) {
        short temperature, humidity;

        System.out.println("parse "+bytes.length);

        short[] sensors = new short[2];
        // 取出温度值
        int iTem = (bytes[10] & 0xFF);
        iTem |= (bytes[11] & 0xFF) << 8;
        humidity = (short) iTem;
        // 取出湿度值
        int iHum = (bytes[12] & 0xFF);
        iHum |= (bytes[13] & 0xFF) << 8;
        temperature = (short) iHum;
        // 存放到数组中
        sensors[0] = temperature;
        sensors[1] = humidity;
        return sensors;
    }

    // 需要处理的数据：F8 00 00 00 08 00 70 37 02 00 92 02 9B FF 35 DE E8
    // 截取需要用到的传感器位数据                     92 02 9B FF
    public static String[] parse2HexStr(String str) {
        // 获取传感器数据字段
        // 将数据转换成十六位二进制数
        int i = 0;
        String[] sensor = new String[2];
        String[] temp = str.split(" ");
        StringBuilder strBuilder = new StringBuilder();
        for(i = 10 ; i < 12 ; i ++)
            strBuilder.append(temp[i]);
        sensor[0] = strBuilder.toString();
        strBuilder = null;
        for(i = 11 ; i < 13 ; i ++)
            strBuilder.append(temp[i]);
        sensor[1] = strBuilder.toString();
        return sensor;
    }

    // 将十六进制数转为有符号二进制数
    public static String parseHexStr2Bin(String hexStr) {
        Integer num = Integer.parseInt(hexStr, 16);
        return Integer.toBinaryString(num);
    }

    // 将十六位二进制数进行高低位交换
    public static long highLowExchange(String binStr) {
        long n = Long.valueOf(binStr);
        long dy = n>>6;                     // 后面的16位
        long de = n - (n >> 16 << 16) << 16;// 前面的16位
        return dy + de;
    }

    // 将十六位二进制数转十六进制
    public static String parseByte2HexStr(String binStr) {
        byte[] buf = binStr.getBytes();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    // 十六进制转十进制
    // 获取传感器数据
    public static double[] parseHexStr2Decimal(String str) {
        double temerature = 1, humidity = 1;
        double []sensor = new double[2];
        sensor[0] = temerature;
        sensor[1] = humidity;
        return sensor;
//        return Long.parseLong(str, 16)/ 10;
    }


    // 解析和获取温湿度数据
    public static double[] getSensorData(String data) {
        String hexStr, binStr;
        // 将数据截取到传感器数据
        //hexStr = parse2HexStr(data);
        // 将十六进制转十六位有符号整型
        //binStr = parseHexStr2Bin(hexStr);
        // 将十六位有符号整型进行高低位交换
        //long newBinStr = highLowExchange(binStr);
        // 将十六位有符号整型转换成十六进制
        //hexStr = parseByte2HexStr(binStr);
        // 将十六进制转换成十进制浮点数
        //double[] decimal = parseHexStr2Decimal(hexStr);
        // 将温湿度数据封装到数组并返回
        double decimal[] = null;
        return decimal;
    }

    /**
     *
     * @param s 从串口扫描出的字符串，此时的字符串是“F12389939DEF”这种格式
     * @return 将 16 进制数值字符串 改成 可以理解的字符串语义 ，比如 "id：1 ,tem:20 etc."
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
    public static String tranDateForm2Number(String date) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < date.length(); i++) {
            if (date.charAt(i)=='-'||date.charAt(i)==' '||date.charAt(i)==':') {
                continue;
            }
            res.append(date.charAt(i));
        }
        return res.toString();
    }
}
