package cn.edu.nwpu.serialport.queue;

public interface Queue {

    int getSize();
    boolean isEmpty();
    void enqueue(byte e);
    byte dequeue();
    byte getFront();
    byte[] dequeueNEle(int n);
}
