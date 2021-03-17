package com.example.contorller;

import com.example.model.UnitSelectionSystem;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "logout", value = "/logout")
public class LogoutServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(UnitSelectionSystem.getInstance().getLoggedInStudent() != null) {
            UnitSelectionSystem.getInstance().logoutStudent();
            UnitSelectionSystem.getInstance().setSearchFilter(null);
        }
        response.sendRedirect("/login");
    }
}