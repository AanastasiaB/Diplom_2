package site.stellarburgers;

import io.qameta.allure.Description;
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
    @DisplayName("ÐÐ¾Ð³Ð¸Ð½ Ñ Ð²Ð°Ð»Ð¸Ð´Ð½ÑÐ¼Ð¸ Ð´Ð°Ð½Ð½ÑÐ¼Ð¸")
    @Description("ÐÑÐ¾Ð²ÐµÑÐºÐ° ÑÑÐ¿ÐµÑÐ½Ð¾Ð¹ Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð°ÑÐ¸Ð¸ Ñ ÐºÐ¾ÑÑÐµÐºÑÐ½ÑÐ¼Ð¸ ÑÑÐµÑÐ½ÑÐ¼Ð¸ Ð´Ð°Ð½Ð½ÑÐ¼Ð¸. ÐÐ¶Ð¸Ð´Ð°ÐµÑÑÑ ÐºÐ¾Ð´ Ð¾ÑÐ²ÐµÑÐ° 200 Ð¸ success=true")
    public void loginUserWithValidData() {
        loginData = UserGenerator.getDefaultLoginData();
        ValidatableResponse responseLogin = User.loginUser(loginData);

        statusCode = responseLogin.extract().statusCode();
        isLoggedIn = responseLogin.extract().path("success");

        Assert.assertEquals("ÐÑÐ¸Ð±ÐºÐ° Ð² ÐºÐ¾Ð´Ðµ Ð¸Ð»Ð¸ ÑÐµÐ»Ðµ Ð¾ÑÐ²ÐµÑÐ°",
                List.of(SC_OK, true),
                List.of(statusCode, isLoggedIn));
    }

    @Test
    @DisplayName("ÐÐ¾Ð³Ð¸Ð½ Ñ Ð½ÐµÐ²ÐµÑÐ½ÑÐ¼ email")
    @Description("ÐÑÐ¾Ð²ÐµÑÐºÐ° Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð°ÑÐ¸Ð¸ Ñ Ð½ÐµÐºÐ¾ÑÑÐµÐºÑÐ½ÑÐ¼ email. ÐÐ¶Ð¸Ð´Ð°ÐµÑÑÑ ÐºÐ¾Ð´ Ð¾ÑÐ²ÐµÑÐ° 401 (Unauthorized) Ð¸ success=false")
    public void loginWithWrongEmail() {
        loginData = UserGenerator.getLoginDataWithWrongEmail();
        ValidatableResponse responseLogin = User.loginUser(loginData);

        statusCode = responseLogin.extract().statusCode();
        isLoggedIn = responseLogin.extract().path("success");

        Assert.assertEquals("ÐÑÐ¸Ð±ÐºÐ° Ð² ÐºÐ¾Ð´Ðµ Ð¸Ð»Ð¸ ÑÐµÐ»Ðµ Ð¾ÑÐ²ÐµÑÐ°",
                List.of(SC_UNAUTHORIZED, false),
                List.of(statusCode, isLoggedIn));
    }

    @Test
    @DisplayName("ÐÐ¾Ð³Ð¸Ð½ Ñ Ð½ÐµÐ²ÐµÑÐ½ÑÐ¼ Ð¿Ð°ÑÐ¾Ð»ÐµÐ¼")
    @Description("ÐÑÐ¾Ð²ÐµÑÐºÐ° Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð°ÑÐ¸Ð¸ Ñ Ð½ÐµÐºÐ¾ÑÑÐµÐºÑÐ½ÑÐ¼ Ð¿Ð°ÑÐ¾Ð»ÐµÐ¼. ÐÐ¶Ð¸Ð´Ð°ÐµÑÑÑ ÐºÐ¾Ð´ Ð¾ÑÐ²ÐµÑÐ° 401 (Unauthorized) Ð¸ success=false")
    public void loginWithWrongPassword() {
        loginData = UserGenerator.getLoginDataWithWrongPassword();
        ValidatableResponse responseLogin = User.loginUser(loginData);

        statusCode = responseLogin.extract().statusCode();
        isLoggedIn = responseLogin.extract().path("success");

        Assert.assertEquals("ÐÑÐ¸Ð±ÐºÐ° Ð² ÐºÐ¾Ð´Ðµ Ð¸Ð»Ð¸ ÑÐµÐ»Ðµ Ð¾ÑÐ²ÐµÑÐ°",
                List.of(SC_UNAUTHORIZED, false),
                List.of(statusCode, isLoggedIn));
    }
}
