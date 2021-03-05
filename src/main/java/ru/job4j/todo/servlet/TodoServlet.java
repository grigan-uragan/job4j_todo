package ru.job4j.todo.servlet;

import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.TodoService;
import ru.job4j.todo.store.ItemStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TodoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        String input = req.getParameter("input");
        TodoService service = new TodoService(new ItemStore());
        if (input != null) {
            Item item = new Item(input);
            service.saveTask(item);
        }
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.println(service.showAllInJSON());
        writer.flush();
    }
}
