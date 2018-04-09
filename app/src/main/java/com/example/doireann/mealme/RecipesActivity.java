package com.example.doireann.mealme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Doireann on 2018-04-06.
 */

public class RecipesActivity extends AppCompatActivity implements RecipesFetchDone {
    Button fetchMore;
    TextView triviaTxt;
    RecipesAdapter adapter = null;
    Bundle b;
    ListView lv;
    int counter = 0;

    private void sendFetchRecipeList(Boolean search, String tags, int offset) {
        final RecipesFetchAsync recipesFetchAsync = new RecipesFetchAsync(this, search, tags, counter);
        recipesFetchAsync.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);


        Intent i = getIntent();
        b = i.getExtras();
        sendFetchRecipeList(b.getBoolean("search", false), b.getString("tag", ""), counter);

        triviaTxt = findViewById(R.id.id_trivia_txt);
        triviaTxt.setText(b.getString("trivia"));
        triviaTxt.postDelayed(new Runnable() {
            @Override
            public void run() {
                triviaTxt.setVisibility(View.GONE);
            }
        }, 4000);


        lv = findViewById(R.id.id_fetch_list);
        adapter = new RecipesAdapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Recipe selectedRecipe = adapter.getSelectedRecipe(position);
                Bundle bDetails = new Bundle();
                bDetails.putString("recipe_id", selectedRecipe.getId());
                bDetails.putString("recipe_title", selectedRecipe.getTitle());
                bDetails.putString("recipe_imageUrl", selectedRecipe.getImageUrl());
                Intent iDetails = new Intent(RecipesActivity.this, DetailsActivity.class);
                iDetails.putExtras(bDetails);
                startActivity(iDetails);
            }
        });

        fetchMore = findViewById(R.id.id_fetch_btn);
        fetchMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                sendFetchRecipeList(b.getBoolean("search", false),
                        b.getString("tag", ""), counter);
//                Toast.makeText(RecipesActivity.this, String.valueOf(counter), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRecipeFetchDone(Recipes recipe_list) {
        adapter.setRecipes(recipe_list);
        adapter.notifyDataSetChanged();
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
// How to access errors from RecipesFetchAsync?
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
