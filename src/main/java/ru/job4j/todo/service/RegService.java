package ru.job4j.todo.service;

import ru.job4j.todo.model.User;
import ru.job4j.todo.store.Store;

public class RegService {
    private Store store;

    public RegService(Store store) {
        this.store = store;
    }

    public void addUser(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        store.saveUser(user);
    }

    public User getUserByEmail(String email, String password) {
        String query = String.format(
                "from User U where U.email = %s and U.password = %s", email, password);
        return store.findUserByEmailAndPassword(email, password);
    }
}
