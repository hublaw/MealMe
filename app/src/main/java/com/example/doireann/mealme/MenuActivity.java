package com.example.doireann.mealme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static android.content.ContentValues.TAG;

public class MenuActivity extends ToolActivity implements TriviaFetchDone {
    private Button search_btn, suggest_btn;
    private EditText search_txt;
    RadioGroup radio_grp;
    RadioButton radio_btn;
    private Intent iSuggest;
    private Intent iSearch;
    private Bundle bSearch;
    private Bundle bSuggest;
    String trivia;
    private Utility util = new Utility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar tbar = findViewById(R.id.id_toolbar);
        setSupportActionBar(tbar);
        getSupportActionBar().setTitle(null);

        bSuggest = new Bundle();
        bSearch = new Bundle();

        new TriviaFetch(MenuActivity.this).execute();
        search_btn = findViewById(R.id.id_search_menu_btn);
        suggest_btn = findViewById(R.id.id_suggest_menu_btn);
        radio_grp = findViewById(R.id.id_suggest_radio_grp);
        search_txt = findViewById(R.id.id_search_ingred_txt);
        search_txt.setShowSoftInputOnFocus(false);

        radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                new TriviaFetch(MenuActivity.this).execute();
                radio_btn = findViewById(checkedId);
                bSuggest.putString("tag", radio_btn.getHint().toString().toLowerCase());
            }
        });

        suggest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TriviaFetch(MenuActivity.this).execute();
                iSuggest = new Intent(MenuActivity.this, RecipesActivity.class);
                bSuggest.putBoolean("search", false);
                iSuggest.putExtras(bSuggest);
                startActivity(iSuggest);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TriviaFetch(MenuActivity.this).execute();

                // input validation and fetch recipe details
                String input = search_txt.getText().toString().replaceAll(" ", ",");
                if (input.matches("[a-zA-Z,]+")) {
                    bSearch.putString("tag", input);
                    bSearch.putBoolean("search", true);
                    iSearch = new Intent(MenuActivity.this, RecipesActivity.class);
                    iSearch.putExtras(bSearch);
                    startActivity(iSearch);
                } else {
                    Toast.makeText(MenuActivity.this, "Please only use letters, spaces or commas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onTriviaFetchDone(String t) {
        bSearch.putString("trivia", t);
        bSuggest.putString("trivia", t);
    }

    @Override
    public Context getTriviaContext() {
        return MenuActivity.this;
    }

    private class TriviaFetch extends AsyncTask<String, String, String> {
        private TriviaFetchDone context;

        public TriviaFetch(Context ctx) {
            super();
            context = (TriviaFetchDone) ctx;
        }

        private void parseRecipeIS(InputStream recipeIS) throws JSONException, IOException {
            String recipeJsonString = util.inputStreamToString(recipeIS);
            JSONObject jsonRootObject = new JSONObject(recipeJsonString);
            trivia = jsonRootObject.getString("text");
        }

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences prefs = context.getTriviaContext().getSharedPreferences("app_state", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("status", "No Exception");
            editor.apply();
            try {
                String triviaUrl = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/trivia/random";
                HttpURLConnection conn = util.createConnection(triviaUrl);
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
                Log.e(TAG, "Exception parsing trivia with id ", je);
                editor.putString("status", "Exception fetching recipes. Please try again");
                editor.apply();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                editor.putString("status", e.getMessage());
                editor.apply();
            }
            return trivia;
        }

        @Override
        protected void onPostExecute(String t) {
//            super.onPostExecute(recipe);
            context.onTriviaFetchDone(t);
        }
    }
}
