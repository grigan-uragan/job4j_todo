package ru.job4j.todo.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.Store;

public class TodoService {
    private Store<Item> store;

    public TodoService(Store<Item> store) {
        this.store = store;
    }

    public String showAllInJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(store.findAll());
    }

    public void saveTask(Item item) {
        store.save(item);
    }
}
