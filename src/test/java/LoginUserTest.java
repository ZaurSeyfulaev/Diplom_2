import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.pojo.UserRequest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static site.nomoreparties.stellarburgers.generator.UserRequestGenerator.getLoginUserRequest;
import static site.nomoreparties.stellarburgers.generator.UserRequestGenerator.getRandomUserRequest;

public class LoginUserTest {
    private UserClient userClient;
    private String email;
    private String password;
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
        email = randomUserRequest.getEmail();
        password = randomUserRequest.getPassword();
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
    @DisplayName("Авторизация")
    @Description("Корректные логин и пароль. Успешная авторизация")
    public void loginUserTest() {
        UserRequest userRequest = getLoginUserRequest(email, password);
        userClient.userLogin(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация")
    @Description("Не корректный  логин и корректный пароль. Ошибка авторизации")
    public void loginUserWithIncorrectEmailTest() {
        UserRequest userRequest = getLoginUserRequest("email", password);
        userClient.userLogin(userRequest)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация")
    @Description("Корректный  логин и не корректный пароль. Ошибка авторизации")
    public void loginUserWithIncorrectPasswordTest() {
        UserRequest userRequest = getLoginUserRequest(email, "password");
        userClient.userLogin(userRequest)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}
