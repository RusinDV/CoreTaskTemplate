package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection;
    private Statement statement;
    private ResultSet result;

    public UserDaoJDBCImpl() {
        connection = Util.getConnectionJDBC();
    }

    public void createUsersTable() {
        if (checkTable()) {
            dropUsersTable();
        }
        String queryCreateTable = "CREATE TABLE IF NOT EXISTS `dbusers`.`usersp` (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,`name` VARCHAR(45) NOT NULL, " +
                "`lastname` VARCHAR(45) NOT NULL, `age` INT NOT NULL,PRIMARY KEY (`id`))";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(queryCreateTable);
            System.out.println("Table successfully created...");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void dropUsersTable() {
        if (checkTable()) {
            String queryDtropTable = "DROP TABLE `dbusers`.`usersp`";
            try {
                statement = connection.createStatement();
                statement.executeUpdate(queryDtropTable);
                System.out.println("Table successfully deleted...");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String querySaveUser = "insert into dbusers.usersp values (default,'" + name + "','" + lastName + "','" + age + "')";
        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.executeUpdate(querySaveUser);
            connection.commit();
            System.out.println("User add in database with name " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeConnection();
        }

    }

    public void removeUserById(long id) {
        String queryDeleteById = "DELETE FROM usersp WHERE id = '" + id + "'";

        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            int i = statement.executeUpdate(queryDeleteById);
            if (i > 0) {
                System.out.println("User successfully deleted with id " + id);
            } else {
                System.out.println("User not found...");
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeConnection();
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new LinkedList<>();
        String queryAllUsers = "SELECT * FROM dbusers.usersp";
        try {
            statement = connection.createStatement();
            result = statement.executeQuery(queryAllUsers);
            while (result.next()) {
                User user = new User(result.getString(2), result.getString(3), result.getByte(4));
                user.setId(result.getLong(1));
                userList.add(user);
            }
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        return userList;
    }

    public void cleanUsersTable() {
        List<User> allUsers = getAllUsers();
        for (User allUser : allUsers) {
            removeUserById(allUser.getId());
        }
    }

    private void closeConnection() {
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkTable() {
        String queryCheckTable = "SELECT COUNT(*) FROM information_schema.Tables WHERE TABLE_NAME = 'usersp' ";
        int i = 0;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryCheckTable);
            if (resultSet.next()) {
                i = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return i > 0 ? true : false;
    }
}
