package com.example.model;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class IOHandler {
    //private HtmlPages htmlPages;

    //public IOHandler(HtmlPages htmlPages) {
        //this.htmlPages = htmlPages;
   // }

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

    /*public String htmlPageHandler(String page, Object data) {
        return switch (page) {
            case "courses" -> htmlPages.coursesPage(data);
            case "course" -> htmlPages.coursePage(data);
            case "plan" -> htmlPages.planPage(data);
            case "profile" -> htmlPages.profilePage(data);
            case "change_plan" -> htmlPages.changePlanPage(data);
            case "submit" -> htmlPages.submitPage(data);
            case "submit_ok" -> htmlPages.submitOkPage(data);
            case "submit_failed" -> htmlPages.submitFailedPage(data);
            case "404" -> htmlPages.NotFoundPage(null);
            default -> null;
        };
    }*/
}