import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HandleIO {

    public JSONArray getCoursesData(String _url) {
        try {
            URL url = new URL(_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            StringBuilder inline = new StringBuilder();
            Scanner sc = new Scanner(url.openStream());
            while(sc.hasNext()) {
                inline.append(sc.nextLine());
            }
            JSONParser parser = new JSONParser();
            JSONArray courses = (JSONArray)parser.parse(String.valueOf(inline));
            sc.close();
            return courses;
        } catch (IOException | ParseException ignored) {return null;}
    }

    public JSONArray getStudentsData(String _url) {
        try {
            URL url = new URL(_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            StringBuilder inline = new StringBuilder();
            Scanner sc = new Scanner(url.openStream());
            while(sc.hasNext()) {
                inline.append(sc.nextLine());
            }
            JSONParser parser = new JSONParser();
            JSONArray courses = (JSONArray)parser.parse(String.valueOf(inline));
            sc.close();
            return courses;
        } catch (IOException | ParseException ignored) {return null;}
    }
}