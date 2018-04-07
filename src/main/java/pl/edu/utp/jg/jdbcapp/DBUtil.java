package pl.edu.utp.jg.jdbcapp;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.*;

public class DBUtil {

    static JdbcConnectionPool cp = JdbcConnectionPool.create("jdbc:h2:./src/main/db/empDB", "user", "");

    public static Connection connectToDB(DBType dbType) throws SQLException {

        Connection conn = null;

        switch (dbType) {
            case H2:
                conn = cp.getConnection();
        }

        return conn;
    }

    public static void createDB() {

        String sql = "CREATE TABLE IF NOT EXISTS employee (" +
                "ID int NOT NULL AUTO_INCREMENT," +
                "Name varchar(50)," +
                "Email varchar(50)," +
                "Salary double," +
                "PRIMARY KEY (ID)" +
                ");";

        try(Connection conn = connectToDB(DBType.H2);
            Statement stmt = conn.createStatement()
        ) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testInsert() {
        String sql = "INSERT INTO employee (Name, Email, Salary) VALUES " +
                "('Jan Kowalski', 'jan.kowalski@gmail.com', 2700)," +
                "('Zbigniew Nowak', 'zibi.nowak@gmail.com', 3400)," +
                "('Anna Yoanna', 'anna.yoanna@gmail.com', 3600);";

        try(Connection conn = connectToDB(DBType.H2);
            Statement stmt = conn.createStatement();
        ) {
            stmt.executeUpdate(sql);
            System.out.println("Test records inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void close() {
        cp.dispose();
    }


}
