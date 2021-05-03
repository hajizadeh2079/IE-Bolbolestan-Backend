package IE.server.controllers;

import IE.server.controllers.models.CourseDTO;
import IE.server.services.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "courses")
public class CoursesController {

    @GetMapping
    public ArrayList<CourseDTO> getCoursesData(@RequestParam String search, @RequestParam String type) {
        return UnitSelectionSystem.getInstance().getFilteredCourses(search, type);
    }
}
