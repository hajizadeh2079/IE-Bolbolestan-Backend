package IE.server.controllers;

import IE.server.controllers.models.LoginDTO;
import IE.server.controllers.models.ResponseDTO;
import IE.server.controllers.models.TokenDTO;
import IE.server.services.UnitSelectionSystem;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import IE.server.repository.models.StudentDAO;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(value = "students")
public class UserController {

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) throws UnsupportedEncodingException {
        String jwt = UnitSelectionSystem.getInstance().createToken(loginDTO.getEmail(), loginDTO.getPassword());
        if(jwt != null)
            return new ResponseEntity<>(new TokenDTO(jwt), HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/signup")
    public ResponseDTO signup(@RequestBody StudentDAO studentDAO) {
        boolean success = UnitSelectionSystem.getInstance().signupStudent(studentDAO);
        return new ResponseDTO(success, "Done!");
    }

    @PostMapping("/password/forget")
    public ResponseDTO forgetPassword(@RequestBody JSONObject jsonObject) {
        String email = (String) jsonObject.get("email");
        boolean success = UnitSelectionSystem.getInstance().forgetPassword(email);
        return new ResponseDTO(success, "Done!");
    }
}
