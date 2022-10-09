package cn.edu.nwpu.disruptor;
import com.lmax.disruptor.ExceptionHandler;
public class QueueEventExceptionHandler implements ExceptionHandler<QueueEvent> {
    @Override
    public void handleEventException(Throwable throwable, long l, QueueEvent queueEvent) {
    }
    @Override
    public void handleOnStartException(Throwable throwable) {
    }
    @Override
    public void handleOnShutdownException(Throwable throwable) {
    }
}
