package IE.server.controllers;

import IE.server.controllers.models.CourseModel;
import IE.server.controllers.models.ResponseModel;
import IE.server.controllers.models.SelectedCourseModel;
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
    public ResponseModel addCourse(@RequestBody CourseModel courseModel) {
        try {
            UnitSelectionSystem.getInstance().addCourse(courseModel.getId(), courseModel.getCode(), courseModel.getClassCode());
            return new ResponseModel(true, "Done!");
        } catch (Exception exception) {
            return new ResponseModel(false, exception.getMessage());
        }
    }

    @GetMapping("/{id}")
    public SelectedCourseModel getCoursesData(@PathVariable String id) {
        try {
            Student student = UnitSelectionSystem.getInstance().findStudent(id);
            ArrayList<Course> finalizedCourses = student.getWeeklySchedule().getFinalizedCourses();
            ArrayList<Course> nonFinalizedCourses = student.getWeeklySchedule().getNonFinalizedCourses();
            ArrayList<Course> waitingCourses = student.getWeeklySchedule().getWaitingCourses();
            int sumOfUnits = student.getWeeklySchedule().sumOfUnits();
            return new SelectedCourseModel(finalizedCourses, nonFinalizedCourses, waitingCourses, sumOfUnits);
        } catch (Exception exception) {
            return null;
        }
    }

    @DeleteMapping
    public ResponseModel deleteCourse(@RequestBody CourseModel courseModel) {
        UnitSelectionSystem.getInstance().removeFromWeeklySchedule(courseModel.getId(), courseModel.getCode(), courseModel.getClassCode());
        return new ResponseModel(true, "Done!");
    }

}
