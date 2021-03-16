package com.example.contorller;

import com.example.model.UnitSelectionSystem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Courses", value = "/courses")
public class CoursesServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "search":
                String searchFilter = request.getParameter("search");
                if (!searchFilter.equals(""))
                    UnitSelectionSystem.getInstance().setSearchFilter(searchFilter);
                break;
            case "clear":
                UnitSelectionSystem.getInstance().setSearchFilter(null);
                break;
        }
        response.sendRedirect("/courses");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = UnitSelectionSystem.getInstance().getLoggedInStudent();
        if (id == null)
            response.sendRedirect("/login");
        else {
            request.setAttribute("std_id", id);
            request.setAttribute("filtered_courses", UnitSelectionSystem.getInstance().getFilteredCourses());
            String searchFilter = UnitSelectionSystem.getInstance().getSearchFilter();
            if (searchFilter == null)
                searchFilter = "";
            request.setAttribute("search_filter", searchFilter);
            request.getRequestDispatcher("/courses.jsp").forward(request, response);
        }
    }
}
