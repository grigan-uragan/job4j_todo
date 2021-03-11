package ru.job4j.todo.store;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.User;

import java.io.File;
import java.util.List;
import java.util.function.Function;

public class UserStore implements Store<User>, AutoCloseable {
    private static final Logger LOG = LogManager.getLogger(UserStore.class);
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure("hibernate.cfg.xml")
            .build();
    private final SessionFactory factory =
            new MetadataSources(registry).buildMetadata().buildSessionFactory();

    @Override
    public User save(User element) {
        tx(session -> session.save(element));
        return element;
    }

    @Override
    public List<User> findAll() {
        return tx(session -> session.createQuery("from ru.job4j.todo.model.User").list());
    }

    @Override
    public void update(User element) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(element);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error("transaction failed", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public User findById(int id) {
        return tx(session -> session.get(User.class, id));
    }

    @Override
    public List<User> allItemWithStatus(boolean status) {
        return null;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    public User findByEmail(String email, String password) {
        return tx(session -> {
            Query query = session.createQuery(
                    "from User U where U.email = :email and U.password = :password");
            query.setParameter("email", email);
            query.setParameter("password", password);
            return (User) query.getSingleResult();
        });
    }

    public <T> T tx(Function<Session, T> command) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            T result = command.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.error("transaction failed", e);
            throw e;
        } finally {
            session.close();
        }
    }
}
