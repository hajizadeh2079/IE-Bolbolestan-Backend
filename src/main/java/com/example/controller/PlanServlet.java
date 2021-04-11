package com.example.controller;
import com.example.model.Course;
import com.example.model.UnitSelectionSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "plan", value = "/plan")
public class PlanServlet  extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = UnitSelectionSystem.getInstance().getLoggedInStudent();
        if (id == null)
            response.sendRedirect("/login");
        else {
            try {
                ArrayList<Course> courses = UnitSelectionSystem.getInstance().findStudent(id).getWeeklySchedule().getLastFinalizedCourses();
                HashMap<String, String> plan = new HashMap<>();
                for(Course course: courses) {
                    for(String day: course.getClassTimeDays()) {
                        plan.put(day + "-" + course.getClassTimeStart(), course.getName());
                    }
                }
                request.setAttribute("std_id", id);
                request.setAttribute("plan", plan);
                request.getRequestDispatcher("/plan.jsp").forward(request, response);
            } catch (Exception ignored) { }
        }
    }
}