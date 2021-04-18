package IE.server.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "")
public class HomeController {

    @GetMapping("/{id}")
    public String getHomeData(@PathVariable String id) {
        return id;
    }
}
