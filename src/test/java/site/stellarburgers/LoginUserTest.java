package site.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import junitparams.JUnitParamsRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import site.stellarburgers.client.User;
import site.stellarburgers.generator.UserGenerator;
import site.stellarburgers.pojo.LoginUser;
import site.stellarburgers.pojo.RegisterUser;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

@RunWith(JUnitParamsRunner.class)
public class LoginUserTest {

    private final RegisterUser registerData = UserGenerator.getDefaultRegistrationData();
    private LoginUser loginData;
    private String token = "";
    private int statusCode;
    private boolean isLoggedIn;

    @Before
    public void setUp() {
        ValidatableResponse responseRegister = User.registerUser(registerData);
        token = responseRegister.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        ValidatableResponse responseDelete = User.deleteUser(token);
    }

    @Test
    @DisplayName("Логин пользователя с валидными данными")
    public void loginUserWithValidData() {
        loginData = UserGenerator.getDefaultLoginData();
        ValidatableResponse responseLogin = User.loginUser(loginData);

        statusCode = responseLogin.extract().statusCode();
        isLoggedIn = responseLogin.extract().path("success");

        Assert.assertEquals("Ошибка в коде или теле ответа", List.of(SC_OK, true),
                List.of(statusCode, isLoggedIn));
    }

    @Test
    @DisplayName("Логин пользователя с неверным логином и паролем")
    public void loginFakeUser() {
        loginData = UserGenerator.getFakeLoginData();
        ValidatableResponse responseLogin = User.loginUser(loginData);

        statusCode = responseLogin.extract().statusCode();
        isLoggedIn = responseLogin.extract().path("success");

        Assert.assertEquals("Ошибка в коде или теле ответа", List.of(SC_UNAUTHORIZED, false),
                List.of(statusCode, isLoggedIn));
    }
}
