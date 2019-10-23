package utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;

public class HttpGet {
    private String name;
    private int round;

    public HttpGet(String name, int round) {
        this.name = name;
        this.round = round;
    }

    public void sendGet() {
        try {
            String url = "http://artii.herokuapp.com/make?text=" + name + "+won+the+game+in+" + round + "+rounds";
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            System.out.println(name + " won the game in " + round + " rounds");
        }

    }
}