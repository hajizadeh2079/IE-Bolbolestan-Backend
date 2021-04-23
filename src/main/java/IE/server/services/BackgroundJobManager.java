package IE.server.services;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.*;

@Configuration
@EnableScheduling
public class BackgroundJobManager {

    @Scheduled(fixedDelay = 120000)
    public void waitListToFinalizedCourse() {
        UnitSelectionSystem.getInstance().waitListToFinalizedCourse();
    }
}