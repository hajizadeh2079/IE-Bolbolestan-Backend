import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class finalTest {

    @Test
    public void test() {
        try {
            UnitSelectionSystem unitSelectionSystem = new UnitSelectionSystem();
            File fileIn = new File("src/test/resources/finalTestIn.txt");
            FileWriter fileOut = new FileWriter("src/test/resources/finalTestOut.txt");
            Scanner scanner = new Scanner(fileIn);
            JSONParser parser = new JSONParser();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line == null)
                    continue;
                fileOut.write(line);
                fileOut.write("\n");
                String command = line.substring(0, line.indexOf(" "));
                String jsonString = line.substring(line.indexOf(" ") + 1);
                JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
                JSONObject response = unitSelectionSystem.doCommand(command, jsonObject);
                if (response != null) {
                    fileOut.write(response.toString());
                    fileOut.write("\n");
                }
            }
            fileOut.close();
        } catch (Exception ignored) { }
    }
}