package cn.edu.nwpu.mysql.Dao;
import cn.edu.nwpu.mysql.pojo.Sensor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import java.sql.SQLException;
import java.util.List;

public class  SensorDao {
    private static ThreadLocal<QueryRunner> qr = new ThreadLocal<>();
    public static QueryRunner getQueryRunner() {
        if(qr.get() == null) {
            QueryRunner temp = new QueryRunner();
            qr.set(temp);
        }
        return qr.get();
    }
    /**
     * To add a sensor object information in the database
     */
    public static void insert(Sensor s){
        //QueryRunner qr = new QueryRunner();
        try {
            getQueryRunner().update(ManagerThreadLocal.getConnection(),
                    "INSERT INTO `sensor`(`sId`, `temperature`, `humidity`,`date`) VALUES (?,?,?,?)",
                    s.getsId(), s.getsTemperature(), s.getsHumidity(),s.getsDate());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ManagerThreadLocal.close();
        }
    }
    /**
     * Modify sensor data based on sensor id
     */
    public static void update(Sensor s) {
        try {
            getQueryRunner().update(ManagerThreadLocal.getConnection(),"UPDATE `sensor` SET `sId` = ?, `date` = ?, `temperature` = ? WHERE `sId` = ?",
                    s.getsId(), s.getsDate(), s.getsTemperature(), s.getsHumidity());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ManagerThreadLocal.close();
        }
    }
    /**
     * Delete database object information based on sensor id
     */
    public static void delete(int id) {
        try {
            getQueryRunner().update(ManagerThreadLocal.getConnection(),"DELETE FROM `sensor` WHERE `id` = ?", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ManagerThreadLocal.close();
        }
    }
    /**
     * 查询表格的方法
     */
    public static List<Sensor> select(){
        List<Sensor> sensors = null;
        try {
            sensors = getQueryRunner().query(ManagerThreadLocal.getConnection(), "SELECT * FROM `sensor`;", new BeanListHandler<>(Sensor.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ManagerThreadLocal.close();
        }
        return sensors;
    }
}
