package cn.edu.nwpu.disruptor;
import cn.edu.nwpu.mysql.pojo.Sensor;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
public class QueueHelper {
    private int handlerCount = 10;
    private int bufferSize = 1024 * 4;
    private Disruptor<QueueEvent> disruptor;
    private QueueEventProducer queueEventProducer;
    public QueueHelper() {
    }
    public void start()
    {
        QueueEventFactory factory = new QueueEventFactory();
        disruptor = new Disruptor<QueueEvent>(factory,
                bufferSize,
                DaemonThreadFactory.INSTANCE,
                ProducerType.MULTI,
                new BlockingWaitStrategy());
        WorkHandler<QueueEvent>[] mysqlWorkHandlers = new WorkHandler[handlerCount];
        for (int i = 0; i < handlerCount; i++) {
            mysqlWorkHandlers[i] = new MySQLEventHandler();
        }

        WorkHandler<QueueEvent>[] mqttWorkHandlers = new WorkHandler[handlerCount];
        for (int i = 0; i < handlerCount; i++) {
            mqttWorkHandlers[i] = new MQTTEventHandler();
        }

        WorkHandler<QueueEvent>[] fabricWorkHandlers = new WorkHandler[handlerCount];
        for (int i = 0; i < handlerCount; i++) {
            fabricWorkHandlers[i] = new FabricEventHandler();
        }
        //Build a relationship between consumers and disruptor
        disruptor
                .handleEventsWithWorkerPool(mysqlWorkHandlers)
                .handleEventsWithWorkerPool(mqttWorkHandlers)
                .handleEventsWithWorkerPool(fabricWorkHandlers)
                .then(new ClearingEventHandler());
        disruptor.setDefaultExceptionHandler(new QueueEventExceptionHandler());
        disruptor.start();
        RingBuffer<QueueEvent> ringBuffer = disruptor.getRingBuffer();
        queueEventProducer = new QueueEventProducer(ringBuffer);
    }
    public void shutdown()
    {
        doHalt();
    }
    private void doHalt()
    {
        disruptor.shutdown();
    }
    public void SendMessageToHandler(Sensor data)
    {
        queueEventProducer.SendMessage(data);
    }
    public int getHandlerCount() {
        return handlerCount;
    }
    public void setHandlerCount(int handlerCount) {
        this.handlerCount = handlerCount;
    }
    public int getBufferSize() {
        return bufferSize;
    }
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
}
