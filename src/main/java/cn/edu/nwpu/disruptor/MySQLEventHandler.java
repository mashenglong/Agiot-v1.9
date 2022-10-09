package cn.edu.nwpu.disruptor;
import cn.edu.nwpu.mysql.Dao.SensorDao;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MySQLEventHandler implements WorkHandler<QueueEvent>, EventHandler<QueueEvent> {
    public MySQLEventHandler() {
    }
    @Override
    public void onEvent(QueueEvent queueEvent, long l, boolean b) throws Exception {
        this.onEvent(queueEvent);
    }
    @Override
    public void onEvent(QueueEvent queueEvent) throws Exception {
        SensorDao.insert(queueEvent.getSensorData());
        log.info("insert data into database, data is : " + queueEvent.getSensorData().toString());
    }
}