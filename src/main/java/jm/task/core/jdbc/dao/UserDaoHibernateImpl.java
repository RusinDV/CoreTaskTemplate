package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.RowSelection;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactoryHibernate();
    }


    @Override
    public void createUsersTable() {
        String queryCreateTable = "CREATE TABLE `dbusers`.`usersp` (`id`  BIGINT(20) NOT NULL AUTO_INCREMENT,`name` VARCHAR(45) NOT NULL, " +
                "`lastname` VARCHAR(45) NOT NULL, `age` INT NOT NULL,PRIMARY KEY (`id`))";
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        if (checkTable()) {
            dropUsersTable();
        }

        try {
            transaction = session.beginTransaction();
            Query query = session.createSQLQuery(queryCreateTable);
            query.executeUpdate();
            transaction.commit();
            System.out.println("Table successfully created...");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                e.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        if (checkTable()) {
            String queryDtropTable = "DROP TABLE `dbusers`.`usersp`";
            Session session = sessionFactory.openSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Query query = session.createSQLQuery(queryDtropTable);
                query.executeUpdate();
                transaction.commit();
                System.out.println("Table successfully deleted...");
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                    e.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
            System.out.println("User add in database with name " + name);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                e.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<User> allUsers = getAllUsers();
            boolean checkDeletedUser = false;
            for (User allUser : allUsers) {
                if (allUser.getId() == id) {
                    User load = session.load(User.class, id);
                    session.delete(load);
                    System.out.println("User successfully deleted with id " + id);
                    checkDeletedUser = true;
                }
            }
            if (!checkDeletedUser) {
                System.out.println("User not deleted, because not found...");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                e.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<User> userList = new LinkedList<>();
        try {
            transaction = session.beginTransaction();
            userList = session.createQuery("SELECT a FROM User a", User.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                e.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        List<User> allUsers = getAllUsers();
        for (User allUser : allUsers) {
            removeUserById(allUser.getId());
        }
    }

    private boolean checkTable() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        String queryCheckTable = "SELECT COUNT(*) as i FROM information_schema.Tables WHERE TABLE_NAME = 'usersp' ";
        int i = 0;
        try {
            transaction = session.beginTransaction();
            i = (int) session.createSQLQuery(queryCheckTable).addScalar("i", IntegerType.INSTANCE).uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                e.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return i > 0 ? true : false;
    }
}
