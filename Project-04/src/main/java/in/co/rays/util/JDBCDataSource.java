package in.co.rays.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * JDBCDataSource is a singleton class that provides database connection pooling using C3P0.
 * <p>
 * It reads database configuration from a resource bundle and manages connections efficiently.
 * It also provides utility methods to close database resources like Connection, Statement, and ResultSet.
 * </p>
 * 
 * @author Akbar
 * @version 1.0
 */
public class JDBCDataSource {

    /** Singleton instance of JDBCDataSource */
    private static JDBCDataSource jds = null;

    /** ComboPooledDataSource instance for connection pooling */
    private ComboPooledDataSource cpds = null;

    /** Resource bundle to read database configuration */
    private static ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.bundle.system");

    /**
     * Private constructor to initialize the connection pool.
     */
    private JDBCDataSource() {

        try {
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass(rb.getString("driver"));
            cpds.setJdbcUrl(rb.getString("url"));
            cpds.setUser(rb.getString("username"));
            cpds.setPassword(rb.getString("password"));
            cpds.setInitialPoolSize(Integer.parseInt(rb.getString("initialpoolsize")));
            cpds.setAcquireIncrement(Integer.parseInt(rb.getString("acquireincrement")));
            cpds.setMaxPoolSize(Integer.parseInt(rb.getString("maxpoolsize")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the singleton instance of JDBCDataSource.
     * 
     * @return JDBCDataSource instance
     */
    public static JDBCDataSource getInstance() {
        if (jds == null) {
            jds = new JDBCDataSource();
        }
        return jds;
    }

    /**
     * Returns a database connection from the connection pool.
     * 
     * @return {@link Connection} object if successful, otherwise null
     */
    public static Connection getConnection() {
        try {
            return getInstance().cpds.getConnection();
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Closes the given database resources.
     * 
     * @param conn the {@link Connection} to close
     * @param stmt the {@link Statement} to close
     * @param rs   the {@link ResultSet} to close
     */
    public static void closeConnection(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the given connection and statement.
     * 
     * @param conn the {@link Connection} to close
     * @param stmt the {@link Statement} to close
     */
    public static void closeConnection(Connection conn, Statement stmt) {
        closeConnection(conn, stmt, null);
    }

    /**
     * Closes the given connection.
     * 
     * @param conn the {@link Connection} to close
     */
    public static void closeConnection(Connection conn) {
        closeConnection(conn, null);
    }

}
