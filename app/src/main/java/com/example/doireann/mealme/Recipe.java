package com.example.doireann.mealme;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Doireann on 2018-04-01.
 */

public class Recipe {
    final String TAG = "DEMO";

    class Ingredient {
        Ingredient(){}

        String name;
        boolean include;
        String image;
    }

    class Dish {
        Dish(){}

        String name;
        String image;
    }

    //----------- Public variables -----------
    public List<Ingredient> ingredients;
    public Dish dish;
    //----------------------------------------





    private static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
