package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.function.Function;

public class DBStore implements Store {
    private static final Logger LOG = LoggerFactory.getLogger(DBStore.class);

    private final SessionFactory factory;

    private DBStore() {
        factory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void saveItem(Item item) {
        tx(session -> session.save(item));
    }

    @Override
    public void saveUser(User user) {
        tx(session -> session.save(user));
    }

    @Override
    public List<Item> findAllItem() {
        return tx(session -> {
            final Query query = session.createQuery(
                    "select distinct i from Item i join fetch i.categories order by i.id");
            return query.list();
        });
    }

    @Override
    public void updateItem(Item element) {
        tx(session ->
            session.createQuery("update Item set done = :isDone where id = :id")
                    .setParameter("isDone", element.isDone())
                    .setParameter("id", element.getId())
                    .executeUpdate()
        );
    }

    @Override
    public Item findItemById(int id) {
        return tx(session -> session.get(Item.class, id));
    }

    @Override
    public List<Item> allNotDoneItem(String query) {
        return tx(session -> session.createQuery(query).list());
    }

    @Override
    public User findUserByEmailAndPassword(String email, String password) {
        return (User) tx(session -> session.createQuery(
                "select u from User u where u.email = :email and u.password = :password")
                .setParameter("email", email)
                .setParameter("password", password)
                .uniqueResult());
    }

    @Override
    public List<Category> allCategory() {
        return tx(session -> session.createQuery("from Category").list());
    }

    @Override
    public Category findCategoryById(int id) {
        return tx(session -> session.get(Category.class, id));
    }

    private <T> T tx(Function<Session, T> command) {
        final Session session = factory.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            T result = command.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            LOG.error("transaction exception", e);
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public static DBStore instOf() {
        return Lazy.INST;
    }

    private static class Lazy {
        private static final DBStore INST = new DBStore();
    }
}
