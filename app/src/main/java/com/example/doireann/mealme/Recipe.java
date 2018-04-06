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
    private String id;
    private String title;
    private String imageUrl;
    private String instructions;
    private List<Ingredient> ingredients;

    public class Ingredient {
        private String originalString;
    }

    public Recipe() {
        ingredients = new ArrayList<Ingredient>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(String id) {
        this.id = id;

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


