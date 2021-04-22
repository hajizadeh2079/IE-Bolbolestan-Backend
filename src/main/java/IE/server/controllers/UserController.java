package IE.server.controllers;

import IE.server.controllers.models.UserModel;
import IE.server.services.UnitSelectionSystem;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "")
public class UserController {

    @GetMapping("students/{id}")
    public UserModel getHomeData(@PathVariable String id) {
        String stdId = "";
        try {
            stdId = UnitSelectionSystem.getInstance().findStudent(id).getId();
        } catch (Exception ignored) { }
        return new UserModel(stdId);
    }
}
