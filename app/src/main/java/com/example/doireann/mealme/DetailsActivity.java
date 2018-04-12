package com.example.doireann.mealme;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;


public class DetailsActivity extends ToolActivity implements DetailsFetchDone, ImageFetch.Listener {
    String url_picture;
    ImageView img;
    TextView instructions;
    ListView lv;
    LinearLayout ll;
    RelativeLayout rl;
    Bundle b;
    private DetailsAdapter adapter;
    private String recipe_id;
    private Recipe recipe;
    private Utility util = new Utility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar tbar = findViewById(R.id.id_toolbar);
        setSupportActionBar(tbar);
        getSupportActionBar().setTitle(null);

        Intent i = getIntent();
        b = i.getExtras();

        // set background colour
        Boolean search = b.getBoolean("search", false);
        ll = findViewById(R.id.id_details_ll);
        rl = findViewById(R.id.id_details_rl);
        Log.d( TAG, String.valueOf(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE));
        if (search) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ll.setBackgroundColor(getResources().getColor(R.color.searchList));
            } else {
                rl.setBackgroundColor(getResources().getColor(R.color.searchList));
            }
        }

        // fetch recipe image
        url_picture = (b.getString("recipe_imageUrl", ""));
        if (url_picture != "") {
            new ImageFetch(this).execute(url_picture);
        }
        RecipeFetch fetcher = new RecipeFetch(this);
        recipe_id = b.getString("recipe_id", "");
        fetcher.execute(null, null, recipe_id);

        // set up layout
        TextView title_txt = findViewById(R.id.id_details_title_txt);
        title_txt.setText(b.getString("recipe_title", ""));
        img = findViewById(R.id.id_details_img);
        lv = findViewById(R.id.id_details_ingredients_list);
        instructions = findViewById(R.id.id_details_instructions_txt);

        // fetch recipe details
        adapter = new DetailsAdapter(this, search);
        lv.setAdapter(adapter);
    }


    @Override
    public void onImageLoaded(Bitmap bitmap) {
        img.setImageBitmap(bitmap);
    }

    @Override
    public void onDetailsFetchDone(Recipe recipe) {
        adapter.setIngredients(recipe);
        adapter.notifyDataSetChanged();
        Log.d(TAG, recipe.getId());

        // manipulate instructions string to add a space after list items
        String spacedInstructs = recipe.getInstructions().replaceAll("\\.<", "\\. <");
        instructions.setText(Html.fromHtml(spacedInstructs));
    }

    private class RecipeFetch extends AsyncTask<String, String, Recipe> {
        private DetailsFetchDone context;

        public RecipeFetch(Context ctx) {
            super();
            context = (DetailsFetchDone) ctx;
        }

        private void parseRecipeIS(InputStream recipeIS) throws JSONException, IOException {
            String recipeJsonString = util.inputStreamToString(recipeIS);
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
                String base_url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
                String id = (b.getString("recipe_id"));
                HttpURLConnection conn = util.createConnection(String.format("%s%s/information", base_url, id));
                InputStream dataInputStream = null;
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    dataInputStream = conn.getInputStream();
                } else {
                    if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                        throw new Exception("Forbidden: " + String.valueOf(responseCode));
                    }
                }
                parseRecipeIS(dataInputStream);
            } catch (IOException ioe) {
                Log.e(TAG, "Exception establishing connection", ioe);
            } catch (JSONException je) {
                Log.e(TAG, "Exception parsing recipe details", je);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return recipe;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Recipe r) {
            context.onDetailsFetchDone(r);
        }
    }
}
