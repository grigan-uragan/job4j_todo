package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
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
        tx(session -> session.save(element));
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
