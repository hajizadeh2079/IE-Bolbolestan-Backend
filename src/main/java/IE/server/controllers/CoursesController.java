package IE.server.controllers;

import IE.server.services.Course;
import IE.server.services.UnitSelectionSystem;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping(value = "courses")
public class CoursesController {

    @GetMapping("")
    public ArrayList<Course> getCoursesData(@RequestParam String search, @RequestParam String type) {
        System.out.println(search);
        System.out.println(type);
        return UnitSelectionSystem.getInstance().getFilteredCourses(search, type);
    }
}
