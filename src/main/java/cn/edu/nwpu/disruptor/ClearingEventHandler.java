package cn.edu.nwpu.disruptor;
import com.lmax.disruptor.EventHandler;
public class ClearingEventHandler implements EventHandler<QueueEvent> {
    public ClearingEventHandler() {
    }
    @Override
    public void onEvent(QueueEvent queueEvent, long l, boolean b) throws Exception {
        queueEvent.clear();
    }
}
