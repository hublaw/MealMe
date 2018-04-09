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
    Recipe recipe = null;
    private Recipes recipes;
    private Context context;
    private int selected_row = -1; // negative means NO row selected;
    private int position;


    public RecipesAdapter(Context context) {
        super(context, 0);
        this.context = context;
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
        this.position = position;
//        if (position == selected_row) {
//            row_view.setBackgroundColor( 0xff06FAEA);
//        }
        TextView title_txt = row_view.findViewById(R.id.id_recipe_title);
        title_txt.setText(recipes.recipeList.get(position).getTitle());
        if(position % 2 == 0) {
            title_txt.setBackgroundResource(R.color.list1);
        } else {
            title_txt.setBackgroundResource(R.color.list2);
        }
        return row_view;
    }

//    public void toggleRowSelection (int position) {
//        // if position matches the already selected row then we unselect it.
//        if (selected_row == position) {
//            selected_row = -1;
//        } else {
//            selected_row = position;
//        }
//        this.notifyDataSetChanged();
//    }

    public Recipe getSelectedRecipe(int i) {
        if (recipes != null) {
            return recipes.recipeList.get(i);
        }
        return null;
    }

    void setRecipes( Recipes r) {
        recipes = r;
    }
}