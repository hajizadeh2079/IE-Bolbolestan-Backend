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
        if(UnitSelectionSystem.getInstance().getLoggedInStudent() != null)
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        else
            request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
