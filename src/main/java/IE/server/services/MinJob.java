package IE.server.services;

public class MinJob implements Runnable {

    @Override
    public void run() {
        UnitSelectionSystem.getInstance().waitListToFinalizedCourse();
    }
}