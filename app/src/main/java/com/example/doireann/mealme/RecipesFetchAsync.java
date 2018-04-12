package com.example.doireann.mealme;

import static android.content.ContentValues.TAG;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;


/**
 * Created by Doireann on 2018-04-05.
 */

public class RecipesFetchAsync extends AsyncTask <String, String, Recipes>{
    private Boolean search;
    private String tags, id, imageUrl = null;
    private int counter;
    private RecipesFetchDone context;
    private Recipes recipes;
    private Utility util = new Utility();


    public RecipesFetchAsync(Context ctx, Boolean search, String tags, int counter) {
        super();
        this.search = search;
        this.tags = tags;
        this.counter = counter;
        context = (RecipesFetchDone) ctx;
    }

    private String createUrlString(Boolean search, String tags) {
        String base_url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
        String options;
        int number = 10;
        if (counter > 0) {
            number = number + counter * 10;
        }
        if (search) {
            options = "findByIngredients?limitLicense=true&number=" + number + "&ranking=1&ingredients=";
        } else {
            options = "random?limitLicense=true&number=" + number + "&tags=";
        }
        return base_url + options + tags;
    }

    private void parseRecipeListIS (InputStream recipeListIS) throws JSONException, IOException {
        String recipeListJsonString = util.inputStreamToString(recipeListIS);
        recipes = new Recipes();
        JSONArray recipeArray;

        if (search) {
            recipeArray = new JSONArray(recipeListJsonString);
        } else {
            JSONObject jsonRootObject = new JSONObject(recipeListJsonString);
            recipeArray = jsonRootObject.getJSONArray("recipes");
        }
        for(int i=0; i<recipeArray.length(); i++) {
            String title;
            Recipe recipe = new Recipe();
            id = recipeArray.getJSONObject(i).getString("id");
            title = recipeArray.getJSONObject(i).getString("title");
            if (recipeArray.getJSONObject(i).has("image")) {
                imageUrl = recipeArray.getJSONObject(i).getString("image");
            }
            if (id != null) recipe.setId(id);
            if (title != null) recipe.setTitle(title);
            if (imageUrl != null) recipe.setImageUrl(imageUrl);
            recipes.recipeList.add(recipe);
        }
    }

    @Override
    protected Recipes doInBackground(String... strings) {
        SharedPreferences prefs = context.getRecipesContext().getSharedPreferences("app_state", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("status", "No Exception");
        editor.apply();
        try {
            String urlString = createUrlString(search, tags);
            HttpURLConnection conn = util.createConnection(urlString);
            InputStream dataInputStream = null;
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                dataInputStream = conn.getInputStream();
            } else {
                if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    throw new Exception("Forbidden: " + String.valueOf(responseCode));
                }
            }
            parseRecipeListIS(dataInputStream);

        } catch (IOException ioe) {
            Log.e(TAG, "Exception establishing connection", ioe);
            editor.putString("status", "Exception establishing connection. Please try again");
            editor.apply();
        } catch (JSONException je) {
            Log.e(TAG, "Exception parsing recipe with id " + id, je);
            editor.putString("status", "Exception fetching recipes. Please try again");
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            editor.putString("status", e.getMessage());
            editor.apply();
        }
        return recipes;
    }

    @Override
    protected void onPostExecute(Recipes recipes) {
        context.onRecipeFetchDone(recipes);
    }
}
