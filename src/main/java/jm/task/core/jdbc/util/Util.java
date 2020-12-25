package jm.task.core.jdbc.util;


import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.Persistence;
import java.sql.*;
import java.util.Properties;

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

    public static SessionFactory getSessionFactoryHibernate() {
        Configuration configuration = new Configuration();
        configuration.setProperty("javax.persistence.jdbc.driver", pathDriverConnection);
        configuration.setProperty("javax.persistence.jdbc.url", dbURLMySQL);
        configuration.setProperty("javax.persistence.jdbc.user", userName);
        configuration.setProperty("javax.persistence.jdbc.password", password);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.addAnnotatedClass(jm.task.core.jdbc.model.User.class).configure();
        return configuration.buildSessionFactory();

    }

    public static void main(String[] args) {
        UserDao userDao = new UserDaoHibernateImpl();
        userDao.saveUser("pety", "pupkin", (byte) 15);
    }
}
