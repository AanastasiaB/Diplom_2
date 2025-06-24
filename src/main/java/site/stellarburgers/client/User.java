package site.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.stellarburgers.pojo.LoginUser;
import site.stellarburgers.pojo.RegisterUser;

import static io.restassured.RestAssured.given;

public class User extends ApiCore {

    private final static String REGISTER_USER_PATH = "api/auth/register";
    private final static String LOGIN_USER_PATH = "api/auth/login";
    private final static String UPDATE_USER_PATH = "api/auth/user";

    @Step("Регистрация нового пользователя")
    public static ValidatableResponse registerUser(RegisterUser data) {
        return given()
                .spec(requestConfig())
                .body(data)
                .when()
                .post(REGISTER_USER_PATH)
                .then();
    }

    @Step("Аутентификация пользователя")
    public static ValidatableResponse loginUser(LoginUser data) {
        return given()
                .spec(requestConfig())
                .body(data)
                .when()
                .post(LOGIN_USER_PATH)
                .then();
    }

    @Step("Обновление данных пользователя")
    public static ValidatableResponse updateUser(RegisterUser data, String bearerPlusToken) {
        return given()
                .spec(requestConfig(bearerPlusToken))
                .body(data)
                .when()
                .patch(UPDATE_USER_PATH)
                .then();
    }

    @Step("Удаление пользователя")
    public static ValidatableResponse deleteUser(String bearerPlusToken) {
        return given()
                .spec(requestConfig(bearerPlusToken))
                .when()
                .delete(UPDATE_USER_PATH)
                .then();
    }
}
