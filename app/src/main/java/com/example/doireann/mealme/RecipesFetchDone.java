package com.example.doireann.mealme;

import android.content.Context;

/**
 * Created by Doireann on 2018-04-11.
 */

public interface RecipesFetchDone {
    void onRecipeFetchDone(Recipes recipe_list);
    Context getRecipesContext();
}
