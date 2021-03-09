package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Item;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public class ItemStore implements Store<Item>, AutoCloseable {
    private StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure(new File("hibernate.cfg.xml"))
            .build();
    private SessionFactory factory = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item save(Item element) {
        Serializable tx = tx(session -> session.save(element));
        System.out.println(tx);
        return element;
    }

    @Override
    public List<Item> findAll() {
        return tx(session -> session.createQuery("from ru.job4j.todo.model.Item").list());
    }

    @Override
    public Item findById(int id) {
        return tx(session1 -> session1.get(Item.class, id));
    }

    @Override
    public void update(Item item) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(item);
            transaction.commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Item> allItemWithStatus(boolean bool) {
        return tx(session -> {
            Query query = session.createQuery("from Item I where I.done = :bool");
            query.setParameter("bool", bool);
            return query.list();
        });
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    private <T> T tx(Function<Session, T> command) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            T result = command.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
