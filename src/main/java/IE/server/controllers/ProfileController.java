package IE.server.controllers;

import IE.server.controllers.models.ProfileDTO;
import IE.server.repository.models.StudentDAO;
import IE.server.services.UnitSelectionSystem;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;

@RestController
@RequestMapping(value = "profiles")
public class ProfileController {

    @GetMapping("/{id}")
    public ProfileDTO getProfileData(@PathVariable String id) {
        try {
            StudentDAO studentDAO = UnitSelectionSystem.getInstance().findStudent(id);
            String name = studentDAO.getName();
            String secondName = studentDAO.getSecondName();
            String stdId = studentDAO.getId();
            String birthDate = studentDAO.getBirthDate();
            DecimalFormat df = new DecimalFormat("0.00");
            String gpa = df.format(UnitSelectionSystem.getInstance().calcGPA(id));
            String tpu = df.format(UnitSelectionSystem.getInstance().calcTPU(id));
            String faculty = studentDAO.getFaculty();
            String field = studentDAO.getField();
            String level = studentDAO.getLevel();
            String status = studentDAO.getStatus();
            String img = studentDAO.getImg();
            return new ProfileDTO(stdId, name, secondName, birthDate, field, faculty, level, status, img, gpa, tpu);
        } catch (Exception ignored) { }
        return null;
    }
}
