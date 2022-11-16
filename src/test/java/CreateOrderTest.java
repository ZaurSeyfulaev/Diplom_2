import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.OrderClient;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.pojo.OrderRequest;
import site.nomoreparties.stellarburgers.pojo.UserRequest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static site.nomoreparties.stellarburgers.generator.OrderRequestGenerator.createOrderGenerator;
import static site.nomoreparties.stellarburgers.generator.UserRequestGenerator.getRandomUserRequest;

public class CreateOrderTest {
    private OrderClient orderClient;
    private UserClient userClient;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
        UserRequest randomUserRequest = getRandomUserRequest();
        token = userClient.createUser(randomUserRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .extract()
                .path("accessToken");
    }

    @After
    public void tearDown() {
        if (token != null) {
            userClient.deleteUser(token)
                    .assertThat()
                    .statusCode(SC_ACCEPTED)
                    .body("success", equalTo(true));
        }

    }


    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа с авторизацией и корректными хэшами")
    public void createOrderWithUserAuthorizationTest() {
        orderClient = new OrderClient();
        OrderRequest orderRequest = createOrderGenerator(5);
        orderClient.createOrderWithAuthorization(orderRequest, token)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));

    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа с авторизацией и некорректным хэшем")
    public void createOrderWithUserAuthorizationWithIncorrectIngredientId() {
        orderClient = new OrderClient();
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setIngredientRequest("222121");
        orderClient.createOrderWithAuthorization(orderRequest, token)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);

    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа без авторизацией и корректными хэшами")
    public void createOrderUserIsNotAuthorized() {
        orderClient = new OrderClient();
        OrderRequest orderRequest = createOrderGenerator(3);
        orderClient.createOrderWithoutAuthorization(orderRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));

    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа с авторизацией без ингредиентов")
    public void createOrderWithoutIngredient() {
        orderClient = new OrderClient();
        OrderRequest orderRequest = new OrderRequest();
        orderClient.createOrderWithoutAuthorization(orderRequest)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));

    }

}
