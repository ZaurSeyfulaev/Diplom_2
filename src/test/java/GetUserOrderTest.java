import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.OrderClient;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.pojo.UserRequest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static site.nomoreparties.stellarburgers.generator.UserRequestGenerator.getRandomUserRequest;

public class GetUserOrderTest {
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
    @DisplayName("Получение заказов")
    @Description("Получение заказов с авторизацией")
    public void getUserOrderWithAuthorizationTest() {
        orderClient = new OrderClient();
        orderClient.getUserOrders(token)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказов")
    @Description("Получение заказов без авторизации")
    public void getUserOrdersWithoutAuthorization() {
        orderClient = new OrderClient();
        String emptyToken = "";
        orderClient.getUserOrders(emptyToken)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
