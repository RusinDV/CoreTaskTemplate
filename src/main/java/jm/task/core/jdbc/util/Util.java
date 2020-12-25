package jm.task.core.jdbc.util;


import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;

import java.sql.*;

public class Util {
    private static String userName = "root";
    private static String password = "12345";
    private static String dbURLMySQL = "jdbc:mysql://127.0.0.1:3306/dbusers?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=GMT";

    public static Connection getConnectionJDBC() {
        Connection connection = null;
        String pathDriverConnection = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(pathDriverConnection);
            return DriverManager.getConnection(dbURLMySQL, userName, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static UserDao getUserDaoImpl() {
        return new UserDaoJDBCImpl();
    }
}
