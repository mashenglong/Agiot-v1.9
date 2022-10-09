package cn.edu.nwpu.mqtt.pool;
public interface ConnectionPoolInter<T> {
    /**
     * Initialize pool resources
     * @param maxActive Maximum number of active connections in the pool
     * @param maxWait maximum waiting time
     */
    void init(Integer maxActive, Long maxWait);
    /**
     * Get resources from the pool
     * @return Connection resources
     * @throws Exception
     */
    T getResource() throws Exception;
    /**
     * release the connection
     * @param connection connection in use
     * @throws Exception
     */
    void release(T connection) throws Exception;
    /**
     * Release connection pool resources
     */
    void close();
}
