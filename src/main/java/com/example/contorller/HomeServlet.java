package com.example.contorller;

import com.example.model.UnitSelectionSystem;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "home", value = "")
public class HomeServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String id = UnitSelectionSystem.getInstance().getLoggedInStudent();
        if (id == null)
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        else {
            request.setAttribute("std_id", id);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
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
            request.setAttribute("bad_id", "false");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("bad_id", "true");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
