package cn.edu.nwpu.serialport.data;

public interface ParseData<E> {

    // Parse byte array by parsing rules
    //调用时，使用多态动态绑定
    public E[] parseData(byte[] bytes);
}
