package uk.ac.tees.w9598552.beautyhealthservice;

public class Recipes {

    String recipe_name,recipe_image,recipe_url;

    public Recipes(String recipe_name, String recipe_image, String recipe_url) {
        this.recipe_name = recipe_name;
        this.recipe_image = recipe_image;
        this.recipe_url = recipe_url;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getRecipe_image() {
        return recipe_image;
    }

    public void setRecipe_image(String recipe_image) {
        this.recipe_image = recipe_image;
    }

    public String getRecipe_url() {
        return recipe_url;
    }

    public void setRecipe_url(String recipe_url) {
        this.recipe_url = recipe_url;
    }
}
