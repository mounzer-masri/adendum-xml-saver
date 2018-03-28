package utils;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by subhi on 11/21/2016.
 */
public class RestClient {

    public static final String GET = "GET";
    public static final String POST = "POST";

    private ArrayList<Pair<String, String>> params;
    private ArrayList<Pair<String, String>> headers;

    private String url;
    private String body = "";
    private String response;
    private String userName;
    private String password;
    private HttpURLConnection con;


    public RestClient(String url) {
        this.url = url;
        params = new ArrayList<Pair<String, String>>();
        headers = new ArrayList<Pair<String, String>>();
    }

    public void AddParam(String name, String value) {
        params.add(new Pair<String, String>(name, value));
    }

    public void AddHeader(String name, String value) {
        headers.add(new Pair<String, String>(name, value));
    }

    public String execute(String method) throws Exception {


        String combinedParams = "";
        if (!params.isEmpty()) {
            combinedParams += "?";
            for (Pair<String, String> p : params) {
                String paramString = p.getKey() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
                ;
                if (combinedParams.length() > 1) {
                    combinedParams += "&" + paramString;
                } else {
                    combinedParams += paramString;
                }
            }
        }

        URL obj = new URL(url + combinedParams);
        con = (HttpURLConnection) obj.openConnection();

        if (this.userName != null && this.password != null) {
            addAuthentication();
        }

        switch (method) {

            case GET: {
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Accept", "application/json");
                break;
            }
            case POST: {

                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream(), "UTF-8");

                wr.write(body);
                wr.flush();
                wr.close();

                break;
            }


        }


        int responseCode = con.getResponseCode();

        BufferedReader in;
        if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST)
            in = new BufferedReader(
                    new InputStreamReader(con.getErrorStream()));

        else
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine + "\n");
        }
        in.close();
        this.response = response.toString();
        System.out.println(url + ">>>" + response);

        return this.response;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addAuthentication() {
        String encoding = new String(  java.util.Base64.getEncoder().encode((this.userName + ":" + this.password).getBytes()));
        con.addRequestProperty("Authorization", "Basic " + encoding);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
