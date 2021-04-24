package IE.server.controllers;

import IE.server.services.Report;
import IE.server.services.UnitSelectionSystem;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping(value = "reports")
public class ReportCardController {

    @GetMapping("/{id}")
    public ArrayList<Report> getReportsData(@PathVariable String id) {
        try {
            return UnitSelectionSystem.getInstance().getGradesHistory(id);
        } catch (Exception ignored) { }
        return null;
    }
}
