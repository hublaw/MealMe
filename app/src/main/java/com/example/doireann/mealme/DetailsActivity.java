package com.example.doireann.mealme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DetailsActivity extends AppCompatActivity implements DetailsFetchDone, ImageFetch.Listener {
    String url_picture;
    ImageView img;
    TextView instructions;
    ListView lv;
    Bundle b;
    String TAG = "MEALME";
    private DetailsAdapter adapter;
    private String recipe_id;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent i = getIntent();
        b = i.getExtras();
        url_picture = (b.getString("recipe_imageUrl", ""));
        if (url_picture != "") {
            new ImageFetch(this).execute(url_picture);
        }
        RecipeFetch fetcher = new RecipeFetch(this);
        recipe_id = b.getString("recipe_id", "");
        fetcher.execute(null, null, recipe_id);

        TextView title_txt = findViewById(R.id.id_details_title_txt);
        title_txt.setText(b.getString("recipe_title", ""));
        img = findViewById(R.id.id_details_img);
        instructions = findViewById(R.id.id_details_instructions_txt);
        lv = findViewById(R.id.id_details_ingredients_list);
        adapter = new DetailsAdapter(this);
        lv.setAdapter(adapter);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        img.setImageBitmap(bitmap);
    }

    @Override
    public void onDetailsFetchDone(Recipe recipe) {
        adapter.setIngredients(recipe);
        Log.d(TAG, recipe.getId());

        adapter.notifyDataSetChanged();
        // manipulate instructions string to add a space after list items
        String spacedInstructs = recipe.getInstructions().replaceAll("\\.<", "\\. <");
        instructions.setText(Html.fromHtml(spacedInstructs));
        Log.d(TAG + "BEFORE", recipe.getInstructions());
        Log.d(TAG + "AFTER", spacedInstructs);
    }

    private class RecipeFetch extends AsyncTask<String, String, Recipe> {
        HttpURLConnection connection;
        private DetailsFetchDone context;

        public RecipeFetch(Context ctx) {
            super();
            context = (DetailsFetchDone) ctx;
        }

        private void createConnection() throws IOException {
            String base_url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
            URL url = new URL(base_url + b.getString("recipe_id", "") + "/information");
            Log.d("URL: ", String.valueOf(url));

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setDoInput(true);
            connection.setRequestProperty("X-Mashape-Key", "xxx");
            connection.setRequestProperty("X-Mashape-Host", "xxx");
        }

        private String inputStreamToString(InputStream is) throws IOException{
            StringBuilder response = new StringBuilder("");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            return response.toString();
        }

        private void parseRecipeIS(InputStream recipeIS) throws JSONException, IOException{
            String recipeJsonString = inputStreamToString(recipeIS);
            JSONObject jsonRootObject = new JSONObject(recipeJsonString);
            recipe = new Recipe();
            recipe.setTitle(jsonRootObject.getString("title"));

            // limit-licence = true in url options so instructions will never be null, but could be an empty string
            if (!jsonRootObject.getString("instructions").equals("")) {
                recipe.setInstructions(jsonRootObject.getString("instructions"));
            } else {
                recipe.setInstructions("No instructions provided.\n\nPlease see " +
                        jsonRootObject.getString("sourceUrl") + " for details");
            }
            recipe.setId(jsonRootObject.getString("id"));

            Recipes.Ingredients ingredients = new Recipes().new Ingredients();
            JSONArray array = jsonRootObject.getJSONArray("extendedIngredients");
            for (int i = 0; i < array.length(); i++) {
                Recipe.Ingredient ingredient = new Recipe().new Ingredient();
                ingredient.setOriginalString(array.getJSONObject(i).getString("originalString"));
                ingredients.ingredientList.add(ingredient);
            }
            recipe.setIngredients(ingredients);
        }

        @Override
        protected Recipe doInBackground(String... strings) {
            try {
                createConnection();
                InputStream dataInputStream = null;
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    dataInputStream = connection.getInputStream();
                } else {
                    if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                        throw new Exception("Forbidden: " + String.valueOf(responseCode));
                    }
                }
                parseRecipeIS(dataInputStream);
            }  catch (IOException ioe) {
                Log.e(TAG, "Exception establishing connection", ioe);
            } catch (JSONException je) {
                Log.e(TAG, "Exception parsing recipe details", je);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return recipe;
        }

        @Override
        protected void onPostExecute(Recipe r) {
//            super.onPostExecute(recipe);
            context.onDetailsFetchDone(r);
        }
    }
}
