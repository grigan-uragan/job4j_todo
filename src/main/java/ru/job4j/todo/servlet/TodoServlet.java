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

public class TodoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        String input = req.getParameter("work");
        TodoService service = new TodoService(DBStore.instOf());
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        if ("isDone".equals(input)) {
            String id = req.getParameter("id").trim();
            int i = Integer.parseInt(id);
            service.changeWorkStatus(id);
            String status = req.getParameter("status");
            writer.println(service.showCriteria(status));
            writer.flush();
        }
        if ("show".equals(input)) {
            String res = req.getParameter("status");
            writer.println(service.showCriteria(res));
            writer.flush();
        }
        if ("showTask".equals(input)) {
            String activeTask = service.showCriteria("false");
            writer.println(activeTask);
            writer.flush();
        }
    }
}
