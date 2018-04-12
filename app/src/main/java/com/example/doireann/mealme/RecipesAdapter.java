package com.example.doireann.mealme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Doireann on 2018-04-06.
 */

public class RecipesAdapter extends ArrayAdapter {
    private Recipes recipes;
    private Context context;
    private Boolean search;

    public RecipesAdapter(Context context, Boolean search) {
        super(context, 0);
        this.context = context;
        this.search = search;
    }

    @Override
    public int getCount() {
        if (recipes == null) {
            return 0;
        } else {
            return recipes.recipeList.size();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((RecipesActivity) context).getLayoutInflater();
        View row_view = inflater.inflate(R.layout.recipes_row_view, null);
        TextView title_txt = row_view.findViewById(R.id.id_recipe_title);
        title_txt.setText(recipes.recipeList.get(position).getTitle());

        // set background color striping
        if(position % 2 == 0) {
             if (search) title_txt.setBackgroundResource(R.color.searchListDark);
             else title_txt.setBackgroundResource(R.color.suggestListDark);
        } else {
            if (search) title_txt.setBackgroundResource(R.color.searchList);
            else title_txt.setBackgroundResource(R.color.suggestList);
        }
        return row_view;
    }

    public Recipe getSelectedRecipe(int i) {
        if (recipes != null) {
            return recipes.recipeList.get(i);
        }
        return null;
    }

    public void setRecipes( Recipes r) {
        recipes = r;
    }
}
