import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        UnitSelectionSystem unitSelectionSystem = new UnitSelectionSystem();
        Scanner scanner = new Scanner(System.in);
        JSONParser parser = new JSONParser();
        while (true) {
            String line = scanner.nextLine();
            if (line == null)
                continue;
            String command = line.substring(0, line.indexOf(" "));
            String jsonString = line.substring(line.indexOf(" ") + 1);
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
                JSONObject response = unitSelectionSystem.doCommand(command, jsonObject);
                if (response != null)
                    System.out.println(response);
            } catch (ParseException ignored) { }
        }
    }
}