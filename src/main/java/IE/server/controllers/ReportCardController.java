package IE.server.controllers;

import IE.server.controllers.models.ReportDTO;
import IE.server.services.UnitSelectionSystem;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "reports")
public class ReportCardController {

    @GetMapping
    public ArrayList<ReportDTO> getReportsData(@RequestAttribute("id") String id) {
        try {
            return UnitSelectionSystem.getInstance().getGradesHistory(id);
        } catch (Exception ignored) { }
        return null;
    }
}
