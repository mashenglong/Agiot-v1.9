package cn.edu.nwpu.disruptor;
import cn.edu.nwpu.mysql.pojo.Sensor;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
public class QueueEventProducer {
    private final RingBuffer<QueueEvent> ringBuffer;
    public QueueEventProducer(RingBuffer<QueueEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }
    private static final EventTranslatorOneArg<QueueEvent, Sensor> TRANSLATOR_ONE_ARG =
            new EventTranslatorOneArg<QueueEvent, Sensor>() {
                /**
                 *
                 * @param queueEvent producer
                 * @param l
                 * @param data Producer setup data
                 */
        @Override
        public void translateTo(QueueEvent queueEvent, long l, Sensor data) {
            queueEvent.setSensorData(data);
        }
    };
    //Producer pushes data to ringBuffer
    public void SendMessage(Sensor sensor) {
        ringBuffer.publishEvent(TRANSLATOR_ONE_ARG,sensor);
    }
}
