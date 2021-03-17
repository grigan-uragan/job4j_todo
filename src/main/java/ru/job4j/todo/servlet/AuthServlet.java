package ru.job4j.todo.servlet;

import ru.job4j.todo.model.User;
import ru.job4j.todo.service.RegService;
import ru.job4j.todo.store.DBStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        RegService service = new RegService(DBStore.instOf());
        User byEmail = service.getUserByEmail(email, password);
        if (byEmail != null) {
            HttpSession session = req.getSession();
            session.setAttribute("user", byEmail);
            resp.sendRedirect(req.getContextPath() + "/index.html");
        } else {
            req.setAttribute("error", "Invalid password or email");
            req.getRequestDispatcher("/todo/login.html").forward(req, resp);
        }
    }
}
