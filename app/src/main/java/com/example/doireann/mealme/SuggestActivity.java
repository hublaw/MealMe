package com.example.doireann.mealme;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SuggestActivity extends AppCompatActivity implements OnGetCompleted, GetImage.Listener {
    private TextView instructionsTxtView, titleTxtView;
    private SharedPreferences prefs;
    private ImageView recipeImgView;
    private String url_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        recipeImgView = findViewById(R.id.id_recipe_img);
        titleTxtView = findViewById(R.id.id_title_txt);
        instructionsTxtView = findViewById(R.id.id_instructions_txt);

        sendGetRequest();
    }

    private void sendGetRequest() {
        final GetRandom getSuggestion = new GetRandom(SuggestActivity.this, this);
        getSuggestion.execute();
    }

    @Override
    public void onGetCompletion() {
        prefs = getSharedPreferences("app_state", Context.MODE_PRIVATE);
        String status = prefs.getString("status", "DEFAULT");
        url_img = prefs.getString("rdm_image_url", "https://spoonacular.com/recipeImages/203586-556x370.jpg");

        if (status.equals("ACHIEVED")) {
            if (prefs.contains("rdm_instructions")) {
                instructionsTxtView.setText(Html.fromHtml(prefs.getString("rdm_instructions", "DEFAULT")));
            } else {
                instructionsTxtView.setText("No instructions found");
            }

            if (prefs.contains("rdm_title")) {

                String title = prefs.getString("rdm_title", "DEFAULT");
                String id = prefs.getString("rdm_id", "DEFAULT");
                titleTxtView.setText(title + id);
            } else {
                titleTxtView.setText("No title found");
            }
            if (prefs.contains("rdm_image_url")) {
                new GetImage(this).execute(url_img);
            }
        }  else {
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        recipeImgView.setImageBitmap(bitmap);
    }
}