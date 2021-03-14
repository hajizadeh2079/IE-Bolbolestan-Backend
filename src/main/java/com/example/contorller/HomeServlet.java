package com.example.contorller;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "home", value = "")
public class HomeServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Object stdId = request.getAttribute("std_id");
        if (stdId == null)
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
