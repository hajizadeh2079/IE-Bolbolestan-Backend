package IE.server.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class IOHandler {

    public JSONArray getData(String _url) {
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
            JSONArray jsonArray = (JSONArray)parser.parse(String.valueOf(inline));
            sc.close();
            con.disconnect();
            return jsonArray;
        } catch (Exception exception) {
            return null;
        }
    }

    public void sendEmail(String email, String data) throws IOException {
        URL url = new URL("http://138.197.181.131:5200/api/send_mail");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("url", data);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        con.getInputStream();
        con.disconnect();
    }
}
