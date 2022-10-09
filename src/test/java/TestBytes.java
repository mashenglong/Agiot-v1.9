import cn.edu.nwpu.utils.ByteUtil;

import java.util.Arrays;

public class TestBytes {
    public static void main(String[] args) {
        //编写程序时，一定要区分 getBytes() 与 ByteUtil.hexStr2Byte() 的区别
        String loraMsg = "F80000000800703702009202010035DEE8";
        System.out.println("getBytes:" +  Arrays.toString(loraMsg.getBytes()));
        byte[] bLoraMsg = ByteUtil.hexStr2Byte(loraMsg);
        System.out.println("ByteUtil.hexStr2Byte: " + Arrays.toString(bLoraMsg));

        String zigbeeMsg = "454E44312074656D706572747572653A31312068756D69646974793A31310D0A";
        System.out.println("getBytes:" +  Arrays.toString(zigbeeMsg.getBytes()));
        byte[] bzigbeeMsg = ByteUtil.hexStr2Byte(zigbeeMsg );
        System.out.println("ByteUtil.hexStr2Byte: " + Arrays.toString(bzigbeeMsg));
    }
}
