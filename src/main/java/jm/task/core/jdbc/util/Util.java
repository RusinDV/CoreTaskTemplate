package jm.task.core.jdbc.util;


import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.Persistence;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class Util {
    private static String userName = "root";
    private static String password = "12345";
    private static String pathDriverConnection = "com.mysql.cj.jdbc.Driver";
    private static String dbURLMySQL = "jdbc:mysql://127.0.0.1:3306/dbusers?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=GMT";



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

    public static SessionFactory getSessionFactoryHibernate() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", pathDriverConnection);
        configuration.setProperty("hibernate.connection.url", dbURLMySQL);
        configuration.setProperty("hibernate.connection.username", userName);
        configuration.setProperty("hibernate.connection.password", password);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        // configuration.setProperty("hibernate.show_sql", "true");
        configuration.addAnnotatedClass(User.class).configure();
        return configuration.buildSessionFactory();
    }

}
