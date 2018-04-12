package com.example.doireann.mealme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Doireann on 2018-04-06.
 */

public class Recipes {
    List<Recipe> recipeList;

    public Recipes() {
        recipeList = new ArrayList<Recipe>();
    }

    public class Ingredients {
        List<Recipe.Ingredient> ingredientList;

        public Ingredients() {
            ingredientList = new ArrayList<Recipe.Ingredient>();
        }
    }
}
