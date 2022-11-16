import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.pojo.UserRequest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static site.nomoreparties.stellarburgers.generator.UserRequestGenerator.*;

public class EditUserTest {
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
    @DisplayName("Изменение email с авторизацией")
    @Description("Email успещно изменен")
    public void emailShouldBeChangedTest() {
        String email = String.format("%s@yandex.ru", RandomStringUtils.randomAlphabetic(6).toLowerCase());
        UserRequest userEditRequest = getEditUserEmailRequest(email);
        userClient.editUserWithAuthorization(userEditRequest, token)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("user.email", equalTo(email));
    }

    @Test
    @DisplayName("Изменение имени с авторизацией")
    @Description("Имя успещно изменен")
    public void nameShouldBeChangedTest() {
        UserRequest userEditRequest = getEditUserNameRequest("ivan");
        userClient.editUserWithAuthorization(userEditRequest, token)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("user.name", equalTo("ivan"));
    }

    @Test
    @DisplayName("Изменение пароля с авторизацией")
    @Description("Пароль успещно изменен")
    public void passwordShouldBeChangedTest() {
        UserRequest userEditRequest = getEditUserPasswordRequest("qwerty123");
        userClient.editUserWithAuthorization(userEditRequest, token)
                .assertThat()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Изменение email без авторизации")
    @Description("Ошибка авторизации")
    public void emailShouldNotBeChangedTest() {
        String email = String.format("%s@yandex.ru", RandomStringUtils.randomAlphabetic(6));
        UserRequest userEditRequest = getEditUserEmailRequest(email);
        userClient.editUserWithoutAuthorization(userEditRequest)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Изменение имени без авторизации")
    @Description("Ошибка авторизации")
    public void nameShouldNotBeChangedTest() {
        UserRequest userEditRequest = getEditUserNameRequest("ivan");
        userClient.editUserWithoutAuthorization(userEditRequest)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Изменение пароля без авторизации")
    @Description("Ошибка авторизации")
    public void passwordShouldNotBeChangedTest() {
        UserRequest userEditRequest = getEditUserPasswordRequest("qwerty123");
        userClient.editUserWithoutAuthorization(userEditRequest)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED);
    }
}
