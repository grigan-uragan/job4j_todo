package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;

import java.util.List;

public interface Store<T> {

   T save(T element);

   List<T> findAll();

   void update(T element);

   T findById(int id);

   List<Item> allItemWithStatus(boolean status);
}
