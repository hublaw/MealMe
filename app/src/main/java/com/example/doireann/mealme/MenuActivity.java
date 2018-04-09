package com.example.doireann.mealme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;


public class MenuActivity extends AppCompatActivity implements TriviaFetchDone {
    private Button search_btn, suggest_btn, help_btn;
    private EditText search_txt;
    RadioGroup radio_grp;
    RadioButton radio_btn;
    private Intent iSuggest;
    private Intent iSearch;
    private Bundle bSearch;
    private Bundle bSuggest;
    String trivia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        bSuggest = new Bundle();
        bSearch = new Bundle();

        new TriviaFetch(MenuActivity.this).execute();
        search_btn = findViewById(R.id.id_search_menu_btn);
        suggest_btn = findViewById(R.id.id_suggest_menu_btn);
        help_btn = findViewById(R.id.id_help_menu_btn);
        radio_grp = findViewById(R.id.id_suggest_radio_grp);
        search_txt = findViewById(R.id.id_search_ingred_txt);
//        search_txt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });


        radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                new TriviaFetch(MenuActivity.this).execute();
                radio_btn = findViewById(checkedId);
                bSuggest.putString("tag", radio_btn.getText().toString().toLowerCase());
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

    private class TriviaFetch extends AsyncTask<String, String, String> {
        HttpURLConnection connection;
        private TriviaFetchDone context;

        public TriviaFetch(Context ctx) {
            super();
            context = (TriviaFetchDone) ctx;
        }

        private void createConnection() throws IOException {
            URL url = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/trivia/random");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setDoInput(true);
            connection.setRequestProperty("X-Mashape-Key", "xxx");
            connection.setRequestProperty("X-Mashape-Host", "xxx");
        }

        private String inputStreamToString(InputStream is) throws IOException {
            StringBuilder response = new StringBuilder("");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            return response.toString();
        }

        private void parseRecipeIS(InputStream recipeIS) throws JSONException, IOException {
            String recipeJsonString = inputStreamToString(recipeIS);
            JSONObject jsonRootObject = new JSONObject(recipeJsonString);
            trivia = jsonRootObject.getString("text");
        }

        @Override
        protected String doInBackground(String... strings) {
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

            } catch (IOException ioe) {
                Log.e(TAG, "Exception establishing connection", ioe);
            } catch (JSONException je) {
                Log.e(TAG, "Exception parsing trivia with id ", je);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
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
