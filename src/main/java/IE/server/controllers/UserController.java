package IE.server.controllers;

import IE.server.controllers.models.ResponseDTO;
import IE.server.controllers.models.UserDTO;
import IE.server.services.UnitSelectionSystem;
import org.springframework.web.bind.annotation.*;
import IE.server.repository.models.StudentDAO;

@RestController
@RequestMapping(value = "students")
public class UserController {

    @GetMapping("/{id}")
    public UserDTO getUserData(@PathVariable String id) {
        String stdId = "";
        try {
            stdId = UnitSelectionSystem.getInstance().findStudent(id).getId();
        } catch (Exception ignored) { }
        return new UserDTO(stdId);
    }

    @PostMapping
    public ResponseDTO addCourse(@RequestBody StudentDAO studentDAO) {
        boolean success = UnitSelectionSystem.getInstance().signupStudent(studentDAO);
        return new ResponseDTO(success, "Done!");
    }
}
