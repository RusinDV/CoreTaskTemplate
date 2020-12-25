package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userDaoJDBC = new UserServiceImpl();
        userDaoJDBC.saveUser("Ваня", "Носов", (byte) 37);
        userDaoJDBC.removeUserById(1);
        userDaoJDBC.removeUserById(3);
        List<User> allUsers = userDaoJDBC.getAllUsers();

        
        for (User allUser : allUsers) {
            System.out.println(allUser);
        }

    }
}
