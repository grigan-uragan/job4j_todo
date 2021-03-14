package ru.job4j.todo.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.Store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TodoService {
    private Store store;

    public TodoService(Store store) {
        this.store = store;
    }

    public String showAllIItem() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(store.findAllItem());
    }

    public void saveTask(Item item) {
        store.saveItem(item);
    }

    public void changeWorkStatus(String id) {
        int i = Integer.parseInt(id);
        Item byId = store.findItemById(i);
        boolean oldValue = byId.isDone();
        byId.setDone(!oldValue);
        store.updateItem(byId);
    }

    public String getById(String id) {
        int i = Integer.parseInt(id);
        Gson gson = new GsonBuilder().create();
        return gson.toJson(store.findItemById(i));
    }

    public String showCriteria(String status) {
       if (status.equals("true")) {
           return showAllIItem();
       } else {
           Gson gson = new GsonBuilder().create();
           return gson.toJson(store.allNotDoneItem(
                   "select distinct i from Item i join fetch i.categories"
                           + " where i.done = false order by i.id"));
       }
    }

    public List<Category> allCategories() {
        return store.allCategory();
    }

    public List<Category> categoryByIds(String[] ids) {
        List<Integer> collect = Arrays
                .stream(ids)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return allCategories()
                .stream()
                .filter(category -> collect.contains(category.getId()))
                .collect(Collectors.toList());
    }

}
