package IE.server.controllers;

import IE.server.controllers.models.OfferingDTO;
import IE.server.controllers.models.ResponseDTO;
import IE.server.controllers.models.ScheduleModel;
import IE.server.controllers.models.SelectedCourseDTO;
import IE.server.services.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "plans")
public class PlanController {

    @GetMapping("/{id}")
    public SelectedCourseDTO getCoursesData(@PathVariable String id) {
        try {
            return UnitSelectionSystem.getInstance().getSelectedCourses(id);
        } catch (Exception exception) {
            return null;
        }
    }
/*
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
*/

    @DeleteMapping("/{id}")
    public ResponseDTO deleteCourse(@PathVariable String id, @RequestBody OfferingDTO OfferingDTO) {
        UnitSelectionSystem.getInstance().removeFromWeeklySchedule(id, OfferingDTO.getCode(), OfferingDTO.getClassCode());
        return new ResponseDTO(true, "Done!");
    }

    @PostMapping("/{id}")
    public ResponseDTO addCourse(@PathVariable String id, @RequestBody OfferingDTO OfferingDTO) {
        try {
            UnitSelectionSystem.getInstance().addCourse(id, OfferingDTO.getCode(), OfferingDTO.getClassCode());
            return new ResponseDTO(true, "Done!");
        } catch (Exception exception) {
            return new ResponseDTO(false, exception.getMessage());
        }
    }
/*
    @PostMapping("submit/{id}")
    public ResponseDTO submitPlan(@PathVariable String id) {
        try {
            UnitSelectionSystem.getInstance().submitPlan(id);
            return new ResponseDTO(true, "Done!");
        } catch (Exception exception) {
            return new ResponseDTO(false, exception.getMessage());
        }
    }

    @PostMapping("reset/{id}")
    public ResponseDTO resetPlan(@PathVariable String id) {
        UnitSelectionSystem.getInstance().resetPlan(id);
        return new ResponseDTO(true, "Done!");
    }
 */
}