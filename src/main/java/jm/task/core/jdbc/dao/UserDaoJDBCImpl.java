package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection;

    public UserDaoJDBCImpl() {
        connection = Util.getConnectionJDBC();
    }

    public void createUsersTable() {
        if (checkTable()) {
            dropUsersTable();
        }
        String queryCreateTable = "CREATE TABLE `dbusers`.`usersp` (`id`  BIGINT(20) NOT NULL AUTO_INCREMENT,`name` VARCHAR(45) NOT NULL, " +
                "`lastname` VARCHAR(45) NOT NULL, `age` INT NOT NULL,PRIMARY KEY (`id`))";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(queryCreateTable);
            System.out.println("Table successfully created...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        if (checkTable()) {
            String queryDtropTable = "DROP TABLE `dbusers`.`usersp`";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(queryDtropTable);
                System.out.println("Table successfully deleted...");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        if(!checkTable()){
            createUsersTable();
        }
        String querySaveUser = "insert into dbusers.usersp values (default,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(querySaveUser)) {
            connection.setAutoCommit(false);
            preparedStatement.setNString(1, name);
            preparedStatement.setNString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.execute();
            connection.commit();
            System.out.println("User add in database with name " + name);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void removeUserById(long id) {
        String queryDeleteById = "DELETE FROM usersp WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryDeleteById)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("User successfully deleted with id " + id);
            } else {
                System.out.println("User not found...");
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new LinkedList<>();
        String queryAllUsers = "SELECT * FROM dbusers.usersp";
        try (Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery(queryAllUsers)) {

            while (result.next()) {
                User user = new User(result.getString(2), result.getString(3), result.getByte(4));
                user.setId(result.getLong(1));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void cleanUsersTable() {
        List<User> allUsers = getAllUsers();
        for (User allUser : allUsers) {
            removeUserById(allUser.getId());
        }
    }


    private boolean checkTable() {
        String queryCheckTable = "SELECT COUNT(*) FROM information_schema.Tables WHERE TABLE_NAME = 'usersp' ";
        int i = 0;
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(queryCheckTable)) {
            if (resultSet.next()) {
                i = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i > 0 ? true : false;
    }
}
