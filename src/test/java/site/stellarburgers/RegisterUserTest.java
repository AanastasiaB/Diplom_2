package site.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;
import site.stellarburgers.client.User;
import site.stellarburgers.generator.UserGenerator;
import site.stellarburgers.pojo.RegisterUser;

import java.util.List;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;

public class RegisterUserTest {

    private RegisterUser registerData;
    private String token = "";
    private int statusCode;
    private boolean isRegistered;

    @Test
    @DisplayName("Создание пользователя с валидными данными")
    @Description("Проверка успешной регистрации пользователя с корректными данными. Ожидается код ответа 200 и success=true")
    public void registerUserWithValidData() {
        registerData = UserGenerator.getDefaultRegistrationData();
        ValidatableResponse responseRegister = User.registerUser(registerData);

        token = responseRegister.extract().path("accessToken");
        statusCode = responseRegister.extract().statusCode();
        isRegistered = responseRegister.extract().path("success");

        ValidatableResponse responseDelete = User.deleteUser(token);

        Assert.assertEquals("Ошибка в коде или теле ответа", List.of(SC_OK, true), List.of(statusCode, isRegistered));
    }

    @Test
    @DisplayName("Создание пользователя с занятым email")
    @Description("Проверка обработки попытки регистрации с уже существующим email. Ожидается код ответа 403 (Forbidden) и success=false")
    public void registerDuplicateUser() {
        registerData = UserGenerator.getDefaultRegistrationData();
        ValidatableResponse responseRegister1 = User.registerUser(registerData);
        ValidatableResponse responseRegister2 = User.registerUser(registerData);

        token = responseRegister1.extract().path("accessToken");
        statusCode = responseRegister2.extract().statusCode();
        isRegistered = responseRegister2.extract().path("success");

        ValidatableResponse responseDelete = User.deleteUser(token);

        Assert.assertEquals("Ошибка в коде или теле ответа", List.of(SC_FORBIDDEN, false), List.of(statusCode, isRegistered));
    }
}
