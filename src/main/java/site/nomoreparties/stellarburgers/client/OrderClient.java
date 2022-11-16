package site.nomoreparties.stellarburgers.client;

import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.pojo.OrderRequest;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {


    private static final String ORDER = "orders";
    private static final String GET_INGREDIENTS = "ingredients";


    public List<String> getIngredientsData() {
        return given()
                .spec(getDefaultRequestSpec())
                .get(GET_INGREDIENTS)
                .then()
                .extract()
                .path("data._id");
    }

    public ValidatableResponse createOrderWithAuthorization(OrderRequest orderRequest, String token) {
        return given()
                .header("Authorization", "Bearer" + token)
                .spec(getDefaultRequestSpec())
                .body(orderRequest)
                .log()
                .all()
                .post(ORDER)
                .then();

    }

    public ValidatableResponse createOrderWithoutAuthorization(OrderRequest orderRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(orderRequest)
                .post(ORDER)
                .then();
    }

    public ValidatableResponse getUserOrders(String token) {
        return given()
                .header("Authorization", "Bearer" + token)
                .spec(getDefaultRequestSpec())
                .get(ORDER)
                .then();
    }

}
