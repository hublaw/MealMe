package com.example.doireann.mealme;

/**
 * Created by Doireann on 2018-04-01.
 */

public class Recipe {
    private String id;
    private String title;
    private String imageUrl;
    private String instructions;
    private Recipes.Ingredients ingredients;

    public class Ingredient {
        private String originalString;

        public String getOriginalString() {
            return originalString;
        }

        public void setOriginalString(String originalString) {
            this.originalString = originalString;
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getInstructions() {
        return instructions;
    }

    public Recipes.Ingredients getIngredients() {
        return ingredients;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setIngredients(Recipes.Ingredients ingredients) {
        this.ingredients = ingredients;
    }
}


