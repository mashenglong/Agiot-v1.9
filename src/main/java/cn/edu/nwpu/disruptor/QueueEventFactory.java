package cn.edu.nwpu.disruptor;
import com.lmax.disruptor.EventFactory;
/**
 * Producer
 * 2.event factory class
 * The event factory defines how to instantiate the defined events
 * and needs to implement the interface EventFactory<T>
 * Disruptor creates an instance of Event in RingBuffer through EventFactory
 */
public class QueueEventFactory implements EventFactory<QueueEvent> {
    @Override
    public QueueEvent newInstance() {
        return new QueueEvent();
    }
}
