package IE.server.controllers;

import IE.server.controllers.models.CourseModel;
import IE.server.controllers.models.ResponseModel;
import IE.server.controllers.models.ScheduleModel;
import IE.server.controllers.models.SelectedCourseModel;
import IE.server.services.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "plans")
public class PlanController {

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

    @GetMapping("finalized/{id}")
    public ScheduleModel getPlanData(@PathVariable String id) {
        try {
            Student student = UnitSelectionSystem.getInstance().findStudent(id);
            ArrayList<Course> lastFinalizedCourses = student.getWeeklySchedule().getLastFinalizedCourses();
            Long maxTerm = student.getReportCard().maxTerm();
            return new ScheduleModel(lastFinalizedCourses, maxTerm + 1);
        } catch (Exception exception) {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseModel deleteCourse(@PathVariable String id, @RequestBody CourseModel courseModel) {
        UnitSelectionSystem.getInstance().removeFromWeeklySchedule(id, courseModel.getCode(), courseModel.getClassCode());
        return new ResponseModel(true, "Done!");
    }

    @PostMapping("/{id}")
    public ResponseModel addCourse(@PathVariable String id, @RequestBody CourseModel courseModel) {
        try {
            UnitSelectionSystem.getInstance().addCourse(id, courseModel.getCode(), courseModel.getClassCode());
            return new ResponseModel(true, "Done!");
        } catch (Exception exception) {
            return new ResponseModel(false, exception.getMessage());
        }
    }

    @PostMapping("submit/{id}")
    public ResponseModel submitPlan(@PathVariable String id) {
        try {
            UnitSelectionSystem.getInstance().submitPlan(id);
            return new ResponseModel(true, "Done!");
        } catch (Exception exception) {
            return new ResponseModel(false, exception.getMessage());
        }
    }

    @PostMapping("reset/{id}")
    public ResponseModel resetPlan(@PathVariable String id) {
        UnitSelectionSystem.getInstance().resetPlan(id);
        return new ResponseModel(true, "Done!");
    }
}