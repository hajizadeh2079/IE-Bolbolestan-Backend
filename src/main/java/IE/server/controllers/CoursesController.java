package IE.server.controllers;

import IE.server.controllers.models.CourseModel;
import IE.server.services.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping(value = "courses")
public class CoursesController {

    @GetMapping
    public ArrayList<Course> getCoursesData(@RequestParam String search, @RequestParam String type) {
        System.out.println(search);
        System.out.println(type);
        return UnitSelectionSystem.getInstance().getFilteredCourses(search, type);
    }

    @PostMapping
    public String addCourse(@RequestBody CourseModel courseModel) {
        try {
            UnitSelectionSystem.getInstance().addCourse(courseModel.getId(), courseModel.getCode(), courseModel.getClassCode());
            return "Done!";
        } catch (Exception exception) {
            return exception.getMessage();
        }
    }
}
