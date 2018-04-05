package com.example.doireann.mealme;

import android.content.Context;
import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Doireann on 2018-04-04.
 */

public class GetRecipeID extends AsyncTask<String, String, String> {
//    private OnGetCompleted fetchListener;
//    private Context context;
//    private SharedPreferences prefs;
//    private SharedPreferences.Editor editor;
//    String id;
//    HttpURLConnection connection;
//
//    public GetRecipeID(OnGetCompleted fetchListener, Context context) {
//        super();
//        this.fetchListener = fetchListener;
//        this.context = context;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        prefs = context.getSharedPreferences("app_state", MODE_PRIVATE);
//    }
//
//    @Override
//    protected void onProgressUpdate(String... values) {
//        super.onProgressUpdate(values);
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//        fetchListener.onGetCompletion();
//    }
//
//    private void createConnection() throws Exception{
//        String base_url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/random?number=1&tags=";
//        String tag = prefs.getString("rdm_tag", "DEFAULT");
//        URL randUrl = new URL(base_url + tag + "&limitLicense=true");
//        Log.d("URL: ", base_url + tag + "&limitLicense=true");
//
//        connection = (HttpURLConnection) randUrl.openConnection();
//        connection.setRequestMethod("GET");
//        connection.setConnectTimeout(15000);
//        connection.setReadTimeout(15000);
//        connection.setDoInput(true);
//        connection.setRequestProperty("X-Mashape-Key", "AN48oxY5iYmsh5MjEN0lVXxcTuwcp1kVQknjsn0qpC7NF5mzWi");
//        connection.setRequestProperty("X-Mashape-Host", "spoonacular-recipe-food-nutrition-v1.p.mashape.com");
//    }
//
//    private String inputStreamToString(InputStream is) {
//        StringBuilder response = new StringBuilder("");
//
//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            String line;
//            while ((line = br.readLine()) != null) {
//                response.append(line);
//            }
//            br.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return response.toString();
//    }
//
//    private boolean parse(String jsonString) {
//        try {
//            JSONObject jsonRootObject = new JSONObject(jsonString);
//
//            JSONArray recipes = jsonRootObject.getJSONArray("recipes");
//
//            //--------- parse json array --------
//            title = recipes.getJSONObject(0).getString("title");
//            instructions = recipes.getJSONObject(0).getString("instructions");
//            img_url = recipes.getJSONObject(0).getString("image");
//            id = recipes.getJSONObject(0).getString("id");
//
//        } catch (JSONException e) {
//            return false;
//        }
//        return true;
//    }
//
    @Override
    protected String doInBackground(String... params) {return "";}
//        editor = prefs.edit();
//        try {
//            createConnection();
//            int responseCode = connection.getResponseCode();
//
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                Log.d("CODE", String.valueOf(responseCode));
//
//                if (parse(inputStreamToString(connection.getInputStream()))) {
//                    editor.putString("status", "ACHIEVED");
//                    editor.putString("rdm_title", title);
//                    editor.putString("rdm_instructions", instructions);
//                    editor.putString("rdm_image_url", img_url);
//                    editor.putString("rdm_id", id);
//                    editor.commit();
//                } else {
//                    throw new Exception("Failed to parse recipe");
//                }
//            } else {
//                if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
//                    throw new Exception("Forbidden: " + String.valueOf(responseCode));
//                }
//            }
//        }  catch (Exception e) {
//            editor.putString("status", e.getMessage()).commit();
//        }
//        return "";
//    }

}
