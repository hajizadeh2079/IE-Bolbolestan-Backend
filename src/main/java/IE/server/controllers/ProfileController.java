package IE.server.controllers;

import IE.server.controllers.models.ProfileModel;
import IE.server.services.Student;
import IE.server.services.UnitSelectionSystem;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "")
public class HomeController {

    @GetMapping("profiles/{id}")
    public ProfileModel getHomeData(@PathVariable String id) {
        try {
            Student student = UnitSelectionSystem.getInstance().findStudent(id);
            String name = student.getName();
            String secondName = student.getSecondName();
            String stdId = student.getId();
            String birthDate = student.getBirthDate();
            String gpa = String.valueOf(student.getReportCard().calcGPA()).substring(0, 5);
            String tpu = String.valueOf(student.getReportCard().calcTPU());
            String faculty = student.getFaculty();
            String field = student.getField();
            String level = student.getLevel();
            String status = student.getStatus();
            String img = student.getImg();
            return new ProfileModel(stdId, name, secondName, birthDate, field, faculty, level, status, img, gpa, tpu);
        } catch (Exception ignored) { }
        return null;
    }
}
