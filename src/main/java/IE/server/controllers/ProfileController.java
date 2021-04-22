package IE.server.controllers;

import IE.server.controllers.models.ProfileModel;
import IE.server.services.Student;
import IE.server.services.UnitSelectionSystem;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;

@CrossOrigin
@RestController
@RequestMapping(value = "profiles")
public class ProfileController {

    @GetMapping("/{id}")
    public ProfileModel getProfileData(@PathVariable String id) {
        try {
            Student student = UnitSelectionSystem.getInstance().findStudent(id);
            String name = student.getName();
            String secondName = student.getSecondName();
            String stdId = student.getId();
            String birthDate = student.getBirthDate();
            DecimalFormat df = new DecimalFormat("0.00");
            String gpa = df.format(student.getReportCard().calcGPA());
            String tpu = df.format(student.getReportCard().calcTPU());
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
