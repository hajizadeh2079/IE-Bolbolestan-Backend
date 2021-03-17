package com.example.contorller;
import com.example.model.UnitSelectionSystem;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "plan", value = "/plan")
public class PlanServlet  extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = UnitSelectionSystem.getInstance().getLoggedInStudent();
        if (id == null)
            response.sendRedirect("/login");
        else {
            request.getRequestDispatcher("/plan.jsp").forward(request, response);
        }
    }
}
