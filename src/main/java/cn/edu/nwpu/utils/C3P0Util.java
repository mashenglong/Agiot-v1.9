package cn.edu.nwpu.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author zr
 * @date 2020-12-18-11:40
 * @description
 **/
public class C3P0Util {
    //得到一个数据源
    private static ComboPooledDataSource dataSource = new ComboPooledDataSource();
    public static ComboPooledDataSource getDataSource() {
        return dataSource;
    }
    //从数据源中得到一个连接对象
    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new ExceptionInInitializerError("服务器错误");
        }
    }
    //关闭资源
    public static void release(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
