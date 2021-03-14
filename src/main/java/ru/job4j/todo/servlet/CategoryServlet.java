package ru.job4j.todo.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.service.TodoService;
import ru.job4j.todo.store.DBStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        TodoService service = new TodoService(DBStore.instOf());
        List<Category> list = service.allCategories();
        resp.setContentType("json");
        Gson gson = new GsonBuilder().create();
        String result = gson.toJson(list);
        resp.getWriter().write(result);
    }
}
