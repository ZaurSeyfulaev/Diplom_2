package site.nomoreparties.stellarburgers.generator;

import org.apache.commons.lang3.RandomStringUtils;
import site.nomoreparties.stellarburgers.pojo.UserRequest;

public class UserRequestGenerator {


    public static UserRequest getRandomUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(String.format("%s@yandex.ru", RandomStringUtils.randomAlphabetic(6)).toLowerCase());
        userRequest.setName(RandomStringUtils.randomAlphabetic(10));
        userRequest.setPassword(RandomStringUtils.randomAlphabetic(10));
        return userRequest;
    }

    public static UserRequest getLoginUserRequest(String email, String password) {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(email);
        userRequest.setPassword(password);
        return userRequest;
    }

    public static UserRequest getEditUserNameRequest(String name) {
        UserRequest userRequest = new UserRequest();
        userRequest.setName(name);
        return userRequest;
    }

    public static UserRequest getEditUserPasswordRequest(String password) {
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword(password);
        return userRequest;
    }

    public static UserRequest getEditUserEmailRequest(String email) {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(email);
        return userRequest;
    }
}

