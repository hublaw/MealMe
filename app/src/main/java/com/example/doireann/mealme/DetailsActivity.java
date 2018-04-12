package com.example.doireann.mealme;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;


public class DetailsActivity extends ToolActivity implements DetailsFetchDone, ImageFetch.Listener {
    private String url_picture;
    private ImageView img;
    private TextView instructions;
    private ListView lv;
    private LinearLayout ll;
    private RelativeLayout rl;
    private Bundle b;
    private DetailsAdapter adapter;
    private String recipe_id;
    private Recipe recipe;
    private Utility util = new Utility();
    private ProgressBar pb_img, pb_ing;

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
        pb_img = findViewById(R.id.id_pb_img);
        pb_ing = findViewById(R.id.id_pb_ing);
        if (search) {
            pb_img.getIndeterminateDrawable().setTint(getResources().getColor(R.color.searchBtn));
            pb_ing.getIndeterminateDrawable().setTint(getResources().getColor(R.color.searchBtn));
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
        pb_img.setVisibility(View.GONE);
    }

    @Override
    public void onDetailsFetchDone(Recipe recipe) {
        pb_ing.setVisibility(View.GONE);

        SharedPreferences prefs = getSharedPreferences("app_state", Context.MODE_PRIVATE);
        String status = prefs.getString("status", "DEFAULT");
        if (status.equals("No Exception")) {
            adapter.setIngredients(recipe);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, status, Toast.LENGTH_LONG).show();
        }

        // manipulate instructions string to add a space after list items
        String spacedInstructs = recipe.getInstructions().replaceAll("\\.<", "\\. <");
        instructions.setText(Html.fromHtml(spacedInstructs));
    }

    @Override
    public Context getTriviaContext() {
        return DetailsActivity.this;
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
            SharedPreferences prefs = context.getTriviaContext().getSharedPreferences("app_state", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("status", "No Exception");
            editor.apply();
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
                editor.putString("status", "Exception establishing connection. Please try again");
                editor.apply();
            } catch (JSONException je) {
                Log.e(TAG, "Exception parsing recipe details", je);
                editor.putString("status", "Exception fetching recipes. Please try again");
                editor.apply();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                editor.putString("status", e.getMessage());
                editor.apply();
            }
            return recipe;
        }

        @Override
        protected void onPostExecute(Recipe r) {
            context.onDetailsFetchDone(r);
        }
    }
}
