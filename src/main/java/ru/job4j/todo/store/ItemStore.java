package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Item;

import java.io.File;
import java.util.List;

public class ItemStore implements Store<Item>, AutoCloseable {
    private StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure(new File("hibernate.cfg.xml"))
            .build();
    private SessionFactory factory = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item save(Item element) {
        Session session = factory.openSession();
        session.beginTransaction();
        session.save(element);
        session.getTransaction().commit();
        session.close();
        return element;
    }

    @Override
    public List<Item> findAll() {
        Session session = factory.openSession();
        session.beginTransaction();
        List list = session.createQuery("from ru.job4j.todo.model.Item").list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    @Override
    public Item findById(int id) {
        Session session = factory.openSession();
        session.beginTransaction();
        Item item = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
