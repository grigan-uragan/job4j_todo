package ru.job4j.todo.service;

import ru.job4j.todo.model.User;
import ru.job4j.todo.store.Store;
import ru.job4j.todo.store.UserStore;

public class RegService {
    private UserStore store;

    public RegService(UserStore store) {
        this.store = store;
    }

    public void addUser(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        store.save(user);
    }

    public User getUserByEmail(String email, String password) {
        return store.findByEmail(email, password);
    }
}
