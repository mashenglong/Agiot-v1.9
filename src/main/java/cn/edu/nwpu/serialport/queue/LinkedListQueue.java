package cn.edu.nwpu.serialport.queue;

import java.util.Arrays;

public class LinkedListQueue implements Queue {
    private class Node{
        public Byte e;
        public Node next;
        public Node(Byte e, Node next){
            this.e = e;
            this.next = next;
        }
        public Node(Byte e){
            this(e, null);
        }
        @Override
        public String toString(){
            return e.toString();
        }

    }
    private Node head, tail;
    private int size;
    public LinkedListQueue(){
        head = null;
        tail = null;
        size = 0;
    }
    @Override
    public int getSize(){
        return size;
    }
    @Override
    public boolean isEmpty(){
        return size == 0;
    }
    @Override
    public void enqueue(byte e){
        if(tail == null){
            tail = new Node(e);
            head = tail;
        }
        else{
            tail.next = new Node(e);
            tail = tail.next;
        }
        size ++;
    }
    @Override
    public byte dequeue(){
        if(isEmpty())
            throw new IllegalArgumentException("Cannot dequeue from an empty queue.");
        Node retNode = head;
        head = head.next;
        retNode.next = null;
        if(head == null)
            tail = null;
        size --;
        return retNode.e;
    }
    @Override
    public byte getFront(){
        if(isEmpty())
            throw new IllegalArgumentException("Queue is empty.");
        // 获取队首元素，即front位置的元素
        return head.e;
    }
    @Override
    public byte[] dequeueNEle(int n) {
        byte[] reE = new byte[n];
        for(int i = 0 ; i < n ; i ++) {
            // 调用n次出队操作
            reE[i] = dequeue();
        }
        return reE;
    }

    public byte[] dequeueNEle_forZigbee(int n) {
        byte[] reE = new byte[n];
        for(int i = 0 ; i < n ; i ++) {
            reE[i] = dequeue();
            //In order to verify dequeue data whether it is correct
            //The correct data is
            //69, 78, 68, **, 32, 116, 101, 109, 112, 101, 114, 116, 117, 114,101, 58, **, **, 32, 104, 117, 109, 105, 100, 105, 116, 121, 58, **, **, 13, 10
            switch (i)
            {
                case 0:
                    if(reE[0]==69)
                        break;
                    else i = -1 ; break;
                case 1:
                    if(reE[1]==78)
                        break;
                    else i = -1 ; break;
                case 3:
                    if(reE[3] >= 49) {
                        break;
                    } else {
                        i = -1 ;break;
                    }
                case 16:
                    if(reE[16] >= 49) {
                        break;
                    } else {
                        i = -1 ;break;
                    }
                case 17:
                    if(reE[17] >= 49) {
                        break;
                    } else {
                        i = -1 ;break;
                    }
                case 30:
                    if(reE[30]==13)
                        break;
                    else i = -1 ; break;
            }
        }
        return reE;
    }
    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append("Queue: front ");

        Node cur = head;
        while(cur != null) {
            res.append(cur + "->");
            cur = cur.next;
        }
        res.append("NULL tail");
        return res.toString();
    }
}
