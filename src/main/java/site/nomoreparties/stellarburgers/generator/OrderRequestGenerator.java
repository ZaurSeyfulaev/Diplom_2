package site.nomoreparties.stellarburgers.generator;

import org.apache.commons.lang3.RandomUtils;
import site.nomoreparties.stellarburgers.client.OrderClient;
import site.nomoreparties.stellarburgers.pojo.OrderRequest;

import java.util.List;

public class OrderRequestGenerator {


    public static List<String> getIngredientsForBurger() {
        OrderClient orderClient = new OrderClient();
        return orderClient.getIngredientsData();
    }

    public static OrderRequest createOrderGenerator(int ingredientCount) {
        OrderRequest orderRequest = new OrderRequest();
        for (int i = 0; i <= ingredientCount - 1; i++) {
            int ingredient = RandomUtils.nextInt(0, getIngredientsForBurger().size() - 1);
            orderRequest.setIngredientRequest(getIngredientsForBurger().get(ingredient));
        }
        return orderRequest;
    }
}
