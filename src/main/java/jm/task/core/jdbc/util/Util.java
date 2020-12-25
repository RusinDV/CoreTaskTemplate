package jm.task.core.jdbc.util;


import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Persistence;
import java.sql.*;

public class Util {
    private static String userName = "root";
    private static String password = "12345";
    private static String pathDriverConnection = "com.mysql.cj.jdbc.Driver";
    private static String dbURLMySQL = "jdbc:mysql://127.0.0.1:3306/dbusers?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=GMT";

    public static UserDao getUserDaoImpl() {
        return new UserDaoJDBCImpl();
    }

    public static Connection getConnectionJDBC() {
        Connection connection = null;
        try {
            Class.forName(pathDriverConnection);
            return DriverManager.getConnection(dbURLMySQL, userName, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void getConnectionHibernate() {
        Configuration configuration = new Configuration();
        configuration.setProperty("javax.persistence.jdbc.driver",pathDriverConnection);
        configuration.setProperty("javax.persistence.jdbc.url",dbURLMySQL);
        configuration.setProperty( "javax.persistence.jdbc.user",userName);
        configuration.setProperty( "javax.persistence.jdbc.password",password);
        configuration.setProperty( "hibernate.dialect","org.hibernate.dialect.MySQLDialect");
        configuration.setProperty( "hibernate.show_sql","true");
        configuration.setProperty( "hibernate.hbm2ddl.auto","create-drop");
        SessionFactory sessionFactory =configuration.configure().buildSessionFactory();

    }
}
