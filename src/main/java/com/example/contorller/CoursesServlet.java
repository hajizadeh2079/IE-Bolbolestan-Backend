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
            case "add":
                String courseCode = request.getParameter("course_code");
                String classCode = request.getParameter("class_code");
                String studentId = UnitSelectionSystem.getInstance().getLoggedInStudent();
                try {
                    UnitSelectionSystem.getInstance().addToWeeklySchedule(studentId, courseCode, classCode);
                } catch (Exception exception) {
                    request.setAttribute("error", exception.getMessage());
                    doGet(request, response);
                }
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
            request.setAttribute("search_filter", UnitSelectionSystem.getInstance().getSearchFilter());
            request.setAttribute("total_selected_units", UnitSelectionSystem.getInstance().getTotalSelectedUnits());
            request.setAttribute("plan_courses", UnitSelectionSystem.getInstance().getPlanCourses());

//            request.setAttribute("error", UnitSelectionSystem.getInstance().getError());
            request.getRequestDispatcher("/courses.jsp").forward(request, response);
        }
    }
}
