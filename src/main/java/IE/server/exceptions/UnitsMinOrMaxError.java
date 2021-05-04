package IE.server.exceptions;

public class UnitsMinOrMaxError extends Exception {
    public UnitsMinOrMaxError(String MinOrMax) {
        super(MinOrMax.equals("Min") ? "عدم رعايت حداقل تعداد واحد" : "عدم رعايت حداكثر تعداد واحد");
    }
}
