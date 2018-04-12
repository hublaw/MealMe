package com.example.doireann.mealme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Doireann on 2018-04-06.
 */

public class RecipesActivity extends ToolActivity implements RecipesFetchDone {
    Button fetchMore;
    TextView triviaTxt;
    ConstraintLayout cl;
    RecipesAdapter adapter = null;
    Bundle b;
    ListView lv;
    Boolean search;
    private int counter = 0;
    LinearLayout ll;
    private String tags;
    ProgressBar pb;


    private void sendFetchRecipeList(Boolean search_, String tags_, int offset) {
        final RecipesFetchAsync recipesFetchAsync = new RecipesFetchAsync(this, search_, tags_, counter);
        recipesFetchAsync.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Toolbar tbar = findViewById(R.id.id_toolbar);
        setSupportActionBar(tbar);
        getSupportActionBar().setTitle(null);

        // fetch recipes
        Intent i = getIntent();
        b = i.getExtras();
        search = b.getBoolean("search", false);
        tags = b.getString("tag", "");
        sendFetchRecipeList(search, tags, counter);

        // set background colours
        ll = findViewById(R.id.id_recipe_ll);
        fetchMore = findViewById(R.id.id_fetch_btn);
        triviaTxt = findViewById(R.id.id_trivia_txt);
        pb = findViewById(R.id.id_pb);

        if (search) {
            ll.setBackgroundColor(getResources().getColor(R.color.searchList));
            fetchMore.setBackgroundColor(getResources().getColor(R.color.searchList));
            fetchMore.setTextColor(getResources().getColor(R.color.searchBtn));
            triviaTxt.setBackgroundColor(getResources().getColor(R.color.searchListDark));
            pb.getIndeterminateDrawable().setTint(getResources().getColor(R.color.searchBtn));
        }

        // set trivia visibility and layout
        String triviaStr = b.getString("trivia");
        int delay = 3000;
        if (triviaStr != null) {
            triviaTxt.setText("Did you know?\n" + triviaStr);
            delay = calcDelay(triviaStr.length());
        }
        cl = findViewById(R.id.id_trivia_layout);
        cl.postDelayed(new Runnable() {
            @Override
            public void run() {
                cl.setVisibility(View.GONE);
            }
        }, delay);

        //set up recipes layout
        lv = findViewById(R.id.id_fetch_list);
        adapter = new RecipesAdapter(this, search);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Recipe selectedRecipe = adapter.getSelectedRecipe(position);
                Bundle bDetails = new Bundle();
                bDetails.putString("recipe_id", selectedRecipe.getId());
                bDetails.putString("recipe_title", selectedRecipe.getTitle());
                bDetails.putString("recipe_imageUrl", selectedRecipe.getImageUrl());
                bDetails.putBoolean("search", search);
                Intent iDetails = new Intent(RecipesActivity.this, DetailsActivity.class);
                iDetails.putExtras(bDetails);
                startActivity(iDetails);
            }
        });

        // fetch more button listener

        fetchMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                sendFetchRecipeList(search, tags, counter);
                pb.setVisibility(View.VISIBLE);
            }
        });
    }

    private int calcDelay(int triviaLength) {
        int delay;
        switch (triviaLength / 50) {
            case 0:
            case 1:
                delay = 3000;
                break;
            case 2:
            case 3:
                delay = 4500;
                break;
            case 4:
            case 5:
                delay = 6000;
                break;
            default:
                delay = 8000;
                break;
        }
        return delay;
    }

    @Override
    public void onRecipeFetchDone(Recipes recipe_list) {
        SharedPreferences prefs = getSharedPreferences("app_state", Context.MODE_PRIVATE);
        String status = prefs.getString("status", "DEFAULT");
        pb.setVisibility(View.GONE);

        if (status.equals("No Exception")) {
            adapter.setRecipes(recipe_list);
            adapter.notifyDataSetChanged();
        }  else {
            Toast.makeText(this, status, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Context getRecipesContext() {
        return RecipesActivity.this;
    }
}
