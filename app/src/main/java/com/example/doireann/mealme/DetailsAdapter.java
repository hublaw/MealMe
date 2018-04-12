package com.example.doireann.mealme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Doireann on 2018-04-07.
 */

public class DetailsAdapter extends ArrayAdapter {

    private Recipes.Ingredients ingredients;
    private Context context;
    private Boolean search;


    public DetailsAdapter(Context context, Boolean search) {
        super(context, 0);
        this.context = context;
        this.search = search;
    }

    @Override
    public int getCount() {
        if (ingredients == null) {
            return 0;
        } else {
            return ingredients.ingredientList.size();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((DetailsActivity) context).getLayoutInflater();
        View row_view = inflater.inflate(R.layout.details_row_view, null);

        TextView ingredient_txt = row_view.findViewById(R.id.id_detail_row_txt);
        ingredient_txt.setText(ingredients.ingredientList.get(position).getOriginalString());

        //background color striping
        if (position % 2 == 0) {
            if (search) ingredient_txt.setBackgroundResource(R.color.searchListDark);
            else ingredient_txt.setBackgroundResource(R.color.suggestListDark);
        } else {
            if (search) ingredient_txt.setBackgroundResource(R.color.searchList);
            else ingredient_txt.setBackgroundResource(R.color.suggestList);        }
        return row_view;
    }

    public void setIngredients(Recipe r) {
        this.ingredients = r.getIngredients();
    }

}

