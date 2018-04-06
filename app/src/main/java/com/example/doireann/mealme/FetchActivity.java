package com.example.doireann.mealme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import java.util.List;

/**
 * Created by Doireann on 2018-04-06.
 */

public class FetchActivity extends AppCompatActivity implements RecipeFetchDone, GetImage.Listener{
    private ImageView recipeImgView;
    private Bundle b;

    private void sendFetchRecipeList(Boolean search, String tags) {
        final FetchRecipeList fetchRecipeList = new FetchRecipeList( this, search, tags);
        fetchRecipeList.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);
        recipeImgView = findViewById(R.id.id_recipe_img);
        Intent i = getIntent();
        b = i.getExtras();
        sendFetchRecipeList(b.getBoolean("search", false), b.getString("tag", ""));
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        recipeImgView.setImageBitmap(bitmap);
    }

    @Override
    public void onRecipeFetchDone(List<Recipe> recipe_list) {
        for (int i = 0; i < recipe_list.size(); i++) {
            Log.d("INFOXXXXXXXXXXXXXX", String.valueOf(recipe_list.get(i).getClass()));
        }
    }
}

//not used but will need
//imports
//import android.widget.TextView;
//
//instance variables
//    private TextView instructionsTxtView, titleTxtView;
//
//onCreate
//        titleTxtView = findViewById(R.id.id_title_txt);
//        instructionsTxtView = findViewById(R.id.id_instructions_txt);
//
// How to access errors from FetchRecipeList?
//    @Override
//    public void onGetCompletion() {
//        prefs = getSharedPreferences("app_state", Context.MODE_PRIVATE);
//        String status = prefs.getString("status", "DEFAULT");
//
//        if (status.equals("ACHIEVED")) {
//
//        }  else {
//            Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
//        }
//    }
