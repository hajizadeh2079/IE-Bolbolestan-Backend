package IE.server.exceptions;

public class PrerequisitesError extends Exception {
    public PrerequisitesError(String prerequisite, String code) {
        super("عدم رعايت پيشنيازی:  " + prerequisite + " --> " + code);
    }
}
