package ru.job4j.todo.store;

import java.util.List;

public interface Store<T> {

   T save(T element);

   List<T> findAll();

   T findById(int id);
}
