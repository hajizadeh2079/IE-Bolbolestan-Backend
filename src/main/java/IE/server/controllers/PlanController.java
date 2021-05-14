package IE.server.controllers;

import IE.server.controllers.models.OfferingDTO;
import IE.server.controllers.models.ResponseDTO;
import IE.server.controllers.models.ScheduleDTO;
import IE.server.controllers.models.SelectedCourseDTO;
import IE.server.services.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "plans")
public class PlanController {

    @GetMapping
    public SelectedCourseDTO getCoursesData(@RequestAttribute("id") String id) {
        try {
            return UnitSelectionSystem.getInstance().getSelectedCourses(id);
        } catch (Exception exception) {
            return null;
        }
    }

    @GetMapping("finalized")
    public ScheduleDTO getPlanData(@RequestAttribute("id") String id) {
        try {
            return UnitSelectionSystem.getInstance().getPlan(id);
        } catch (Exception exception) {
            return null;
        }
    }

    @DeleteMapping
    public ResponseDTO deleteCourse(@RequestAttribute("id") String id, @RequestBody OfferingDTO OfferingDTO) {
        UnitSelectionSystem.getInstance().removeFromWeeklySchedule(id, OfferingDTO.getCode(), OfferingDTO.getClassCode());
        return new ResponseDTO(true, "Done!");
    }

    @PostMapping
    public ResponseDTO addCourse(@RequestAttribute("id") String id, @RequestBody OfferingDTO OfferingDTO) {
        try {
            UnitSelectionSystem.getInstance().addCourse(id, OfferingDTO.getCode(), OfferingDTO.getClassCode());
            return new ResponseDTO(true, "Done!");
        } catch (Exception exception) {
            return new ResponseDTO(false, exception.getMessage());
        }
    }

    @PostMapping("submit")
    public ResponseDTO submitPlan(@RequestAttribute("id") String id) {
        try {
            UnitSelectionSystem.getInstance().submitPlan(id);
            return new ResponseDTO(true, "Done!");
        } catch (Exception exception) {
            return new ResponseDTO(false, exception.getMessage());
        }
    }

    @PostMapping("reset")
    public ResponseDTO resetPlan(@RequestAttribute("id") String id) {
        UnitSelectionSystem.getInstance().resetPlan(id);
        return new ResponseDTO(true, "Done!");
    }
}