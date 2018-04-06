package com.example.doireann.mealme;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Doireann on 2018-04-05.
 */

public class FetchRecipeList extends AsyncTask <String, String, List<Recipe>>{
    final String TAG = "MEALME";
    private Boolean search;
    private String tags;
    private RecipeFetchDone context;
    private HttpURLConnection connection;
    private List<Recipe> recipes;
    private List<Recipe.Ingredient> ingredients;

    public FetchRecipeList(Context ctx, Boolean search, String tags) {
        super();
        this.search = search;
        this.tags = tags;
        context = (RecipeFetchDone) ctx;
    }

    private boolean parseRecipeListIS(InputStream recipeListIS) {
        String recipeListJsonString = inputStreamToString(recipeListIS);
        recipes = new ArrayList<Recipe>();
        JSONArray recipeArray;

        try {
            if (search) {
                recipeArray = new JSONArray(recipeListJsonString);
            } else {
                JSONObject jsonRootObject = new JSONObject(recipeListJsonString);
                recipeArray = jsonRootObject.getJSONArray("recipes");
            }

            for(int i=0; i<recipeArray.length(); i++) {
                Recipe recipe = new Recipe();
                recipe.setId(recipeArray.getJSONObject(i).getString("id"));
                recipe.setTitle(recipeArray.getJSONObject(i).getString("title"));
                recipe.setImageUrl(recipeArray.getJSONObject(i).getString("image"));
                recipes.add(recipe);
                Log.d(TAG, recipe.getId());
                Log.d(TAG, recipe.getTitle());
            }
        }
        catch (JSONException e) {
            return false;
        }
        return true;
    }

    private String createUrlString(Boolean search, String tags) {
        String base_url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
        String options;
        if (search) {
            options = "findByIngredients?limitLicense=true&number=10&ranking=1&ingredients=";
        } else {
            options = "random?limitLicense=true&number=10&tags=";
        }
        return base_url + options + tags;
    }

    private void createConnection() throws Exception{
        URL url = new URL(createUrlString(search, tags));
        Log.d("URL: ", String.valueOf(url));

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setDoInput(true);
        connection.setRequestProperty("X-Mashape-Key", "xxx");
        connection.setRequestProperty("X-Mashape-Host", "xxx");
    }

    private String inputStreamToString(InputStream is) {
        StringBuilder response = new StringBuilder("");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    @Override
    protected List<Recipe> doInBackground(String... strings) {
        InputStream dataInputStream = null;
        try {
            createConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                dataInputStream = connection.getInputStream();
            } else {
                if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    throw new Exception("Forbidden: " + String.valueOf(responseCode));
                }
            }
            if (parseRecipeListIS(dataInputStream)) {
                return recipes;
            } else {
                throw new Exception("Failed to parse recipe");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in GetData", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {
        context.onRecipeFetchDone(recipes);
    }
}

// error handling maybe (need to handle app crash when something weird happens in get)
//        try {
//                createConnection();
//                int responseCode = connection.getResponseCode();
//
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                Log.d("CODE", String.valueOf(responseCode));
//
//                if (parse(inputStreamToString(connection.getInputStream()))) {
//                editor.putString("status", "ACHIEVED");
//                editor.putString("rdm_title", title);
//                editor.putString("rdm_instructions", instructions);
//                editor.putString("rdm_image_url", img_url);
//                editor.putString("rdm_id", id);
//                editor.commit();
//                } else {
//                throw new Exception("Failed to parse recipe");
//                }
//                } else {
//                if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
//                throw new Exception("Forbidden: " + String.valueOf(responseCode));
//                }
//                }
//                }  catch (Exception e) {
//                editor.putString("status", e.getMessage()).commit();
//                }