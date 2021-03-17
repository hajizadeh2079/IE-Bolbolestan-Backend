package com.example.contorller;

import com.example.model.Student;
import com.example.model.UnitSelectionSystem;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "profile", value = "/profile")
public class ProfileServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = UnitSelectionSystem.getInstance().getLoggedInStudent();
        if (id == null)
            response.sendRedirect("/login");
        else {
            try {
                Student student = UnitSelectionSystem.getInstance().findStudent(id);
                request.setAttribute("student", student);
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
            } catch (Exception ignored) { }
        }
    }
}