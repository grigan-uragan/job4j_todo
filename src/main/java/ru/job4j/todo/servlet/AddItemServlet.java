package ru.job4j.todo.servlet;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TodoService;
import ru.job4j.todo.store.DBStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AddItemServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        TodoService service = new TodoService(DBStore.instOf());
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        String desc = req.getParameter("input");
        String status = req.getParameter("status");
        String[] category = req.getParameterValues("category[]");
        User user = (User) req.getSession().getAttribute("user");
        Item item = new Item(desc);
        List<Category> categories = service.categoryByIds(category);
        if (!categories.isEmpty()) {
           categories.forEach(item::addCategory);
        }
        item.setUser(user);
        service.saveTask(item);
        writer.println(service.showCriteria(status));
        writer.flush();
    }
}
