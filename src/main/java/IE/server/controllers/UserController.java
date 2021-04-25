package IE.server.controllers;

import IE.server.controllers.models.UserModel;
import IE.server.services.UnitSelectionSystem;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "students")
public class UserController {

    @GetMapping("/{id}")
    public UserModel getUserData(@PathVariable String id) {
        String stdId = "";
        try {
            stdId = UnitSelectionSystem.getInstance().findStudent(id).getId();
        } catch (Exception ignored) { }
        return new UserModel(stdId);
    }
}
