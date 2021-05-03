package IE.server.controllers;

import IE.server.controllers.models.UserDTO;
import IE.server.services.UnitSelectionSystem;
import org.springframework.web.bind.annotation.*;

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
}
