package cn.edu.nwpu.mysql.Dao;
import cn.edu.nwpu.utils.C3P0Util;
import java.sql.Connection;
import java.sql.SQLException;
public class ManagerThreadLocal {
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();
    public static Connection getConnection(){
        //Take a connection from the current thread
        Connection conn = tl.get();
        if (conn == null){
            //take one from the pool
            conn = C3P0Util.getConnection();
            tl.set(conn);
        }
        return conn;
    }
    public static void startTransaction(){
        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void commit(){
        try {
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void rollback(){
        try {
            getConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void close(){
        try {
            getConnection().close();
            tl.remove();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
