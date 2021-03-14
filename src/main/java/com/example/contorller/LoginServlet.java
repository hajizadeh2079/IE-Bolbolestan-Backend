package com.example.contorller;

import com.example.model.UnitSelectionSystem;

import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {

    private String id;

    public void init() {
        id = null;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String temp;
        temp = request.getParameter("std_id");
        try {
            UnitSelectionSystem.getInstance().findStudent(temp);
            id = temp;
            request.setAttribute("std_id", id);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("error!");
        }
    }
}
