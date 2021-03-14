package ru.job4j.todo.store;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.List;

public interface Store {

    void saveItem(Item item);

    void saveUser(User user);

   List<Item> findAllItem();

   void updateItem(Item element);

   Item findItemById(int id);

   List<Item> allNotDoneItem(String query);

   User findUserByEmailAndPassword(String email, String password);

   List<Category> allCategory();

   Category findCategoryById(int id);
}
