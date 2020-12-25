package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userDaoJDBC = new UserServiceImpl();
        userDaoJDBC.createUsersTable();
        userDaoJDBC.saveUser("Петр", "Пупкин", (byte) 40);
        userDaoJDBC.saveUser("Иван", "Носов", (byte) 37);
        userDaoJDBC.saveUser("Светлана", "Носова", (byte) 36);
        userDaoJDBC.saveUser("Петр", "Котов", (byte) 30);
        List<User> allUsers = userDaoJDBC.getAllUsers();
        for (User allUser : allUsers) {
            System.out.println(allUser);
        }
        userDaoJDBC.cleanUsersTable();
        userDaoJDBC.dropUsersTable();

    }
}
