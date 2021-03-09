package ru.job4j.todo.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.Store;

import java.util.List;

public class TodoService {
    private Store<Item> store;

    public TodoService(Store<Item> store) {
        this.store = store;
    }

    public String showAllInJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(store.findAll());
    }

    public Item saveTask(Item item) {
       return store.save(item);
    }

    public void changeWorkStatus(String id) {
        int i = Integer.parseInt(id);
        Item byId = store.findById(i);
        boolean oldValue = byId.isDone();
        byId.setDone(!oldValue);
        store.update(byId);
    }

    public String getById(String id) {
        int i = Integer.parseInt(id);
        Gson gson = new GsonBuilder().create();
        return gson.toJson(store.findById(i));
    }

    public String showCriteria(String status) {
       if (status.equals("true")) {
           return showAllInJSON();
       } else {
           Gson gson = new GsonBuilder().create();
           return gson.toJson(store.allItemWithStatus(false));
       }
    }
}
