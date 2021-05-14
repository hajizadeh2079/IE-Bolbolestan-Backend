package IE.server.controllers;

import IE.server.controllers.models.LoginDTO;
import IE.server.controllers.models.ResponseDTO;
import IE.server.services.UnitSelectionSystem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import IE.server.repository.models.StudentDAO;

import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping(value = "students")
public class UserController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) throws UnsupportedEncodingException {
        String jwt = UnitSelectionSystem.getInstance().createToken(loginDTO.getEmail(), loginDTO.getPassword());
        if(jwt != null)
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/signup")
    public ResponseDTO signup(@RequestBody StudentDAO studentDAO) {
        boolean success = UnitSelectionSystem.getInstance().signupStudent(studentDAO);
        return new ResponseDTO(success, "Done!");
    }
}
