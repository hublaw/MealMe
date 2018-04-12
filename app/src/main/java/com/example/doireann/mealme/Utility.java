package com.example.doireann.mealme;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Doireann on 2018-04-10.
 */

public class Utility {
    final private String token = "xxx";

    public String getToken() {
        return token;
    }

    public String inputStreamToString(InputStream is) throws IOException {
        StringBuilder response = new StringBuilder("");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();
        return response.toString();
    }

    public HttpURLConnection createConnection(String urlString) throws IOException{
        URL url = new URL(urlString);
        Log.d("URL: ", String.valueOf(url));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setDoInput(true);
        connection.setRequestProperty("X-Mashape-Key", getToken());
        connection.setRequestProperty("X-Mashape-Host", "spoonacular-recipe-food-nutrition-v1.p.mashape.com");
        return connection;
    }
}
