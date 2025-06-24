package site.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import site.stellarburgers.client.User;
import site.stellarburgers.generator.UserGenerator;
import site.stellarburgers.pojo.RegisterUser;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

@RunWith(JUnitParamsRunner.class)
public class UpdateUserTest {

    private final RegisterUser registerData = UserGenerator.getDefaultRegistrationData();
    private final RegisterUser updateData = UserGenerator.getDefaultUpdateData();
    private String token = "";
    private int statusCode;
    private boolean isUpdated;

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
    @Parameters(method = "updateUserWithAuthorizationParameters")
    @TestCaseName("Изменение данных пользователя с авторизацией: {0}")
    @DisplayName("Обновление данных пользователя")
    @Description("Проверка обновления данных пользователя с авторизацией и без. " +
            "Ожидается успешное обновление при наличии токена (код 200) " +
            "и ошибка при отсутствии авторизации (код 401)")
    public void updateUserWithAuthorization(boolean isAuth, int status) {
        String token2 = "abc";
        if (isAuth) {
            token2 = token;
        }
        ValidatableResponse responseUpdate = User.updateUser(updateData, token2);

        statusCode = responseUpdate.extract().statusCode();
        isUpdated = responseUpdate.extract().path("success");

        Assert.assertEquals("Ошибка в коде или теле ответа", List.of(status, isAuth), List.of(statusCode, isUpdated));
    }

    private Object[][] updateUserWithAuthorizationParameters() {
        return new Object[][]{
                {true, SC_OK},
                {false, SC_UNAUTHORIZED},
        };
    }
}
