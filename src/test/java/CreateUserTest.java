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
import static site.nomoreparties.stellarburgers.generator.UserRequestGenerator.getRandomUserRequest;

public class CreateUserTest {
    private UserClient userClient;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if (token != null) {
            userClient.deleteUser(token)
                    .assertThat()
                    .statusCode(SC_ACCEPTED)
                    .body("success", is(true));
        }

    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Проверяю корректное создание пользователя.")
    public void createUserTest() {
        UserRequest randomUserRequest = getRandomUserRequest();
        token = userClient.createUser(randomUserRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");

    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Проверка уникальности передаваемых атрибутов пользователя. Ошибка создания пользователя")
    public void userMustNotBeCreatedLoginIsNotUniqueTest() {
        UserRequest randomUserRequest = getRandomUserRequest();
        token = userClient.createUser(randomUserRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
        userClient.createUser(randomUserRequest)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Проверка создания пользователя с пустым email. Ошибка создания пользователя")
    public void userMustNotBeCreatedEmailIsEmptyTest() {
        UserRequest randomUserRequest = getRandomUserRequest();
        randomUserRequest.setEmail("");
        userClient.createUser(randomUserRequest)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Проверка создания пользователя с пустым name. Ошибка создания пользователя")
    public void userMustNotBeCreatedNameIsEmptyTest() {
        UserRequest randomUserRequest = getRandomUserRequest();
        randomUserRequest.setName("");
        userClient.createUser(randomUserRequest)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Проверка создания пользователя с пустым password. Ошибка создания пользователя")
    public void userMustNotBeCreatedPasswordIsEmptyTest() {
        UserRequest randomUserRequest = getRandomUserRequest();
        randomUserRequest.setPassword("");
        userClient.createUser(randomUserRequest)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
