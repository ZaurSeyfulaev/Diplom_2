package site.nomoreparties.stellarburgers.pojo;

import java.util.ArrayList;
import java.util.List;

public class OrderRequest {
    private List<String> ingredients = new ArrayList<>();

    public List<String> getIngredientRequest() {
        return ingredients;
    }

    public void setIngredientRequest(String ingredient) {
        ingredients.add(ingredient);
    }
}
