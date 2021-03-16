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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String id = UnitSelectionSystem.getInstance().getLoggedInStudent();
        if (id != null)
            response.sendRedirect("/");
        else
            request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id;
        id = request.getParameter("std_id");
        if(id.equals("")) {
            request.setAttribute("bad_id", "true");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
        try {
            UnitSelectionSystem.getInstance().findStudent(id);
            UnitSelectionSystem.getInstance().setLoggedInStudent(id);
            response.sendRedirect("/");
        } catch (Exception e) {
            request.setAttribute("bad_id", "true");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
