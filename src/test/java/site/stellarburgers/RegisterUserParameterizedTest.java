package site.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import site.stellarburgers.client.User;
import site.stellarburgers.generator.UserGenerator;
import site.stellarburgers.pojo.RegisterUser;

import java.util.List;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;

@RunWith(JUnitParamsRunner.class)
public class RegisterUserParameterizedTest {

    private RegisterUser registerData;
    private int statusCode;
    private boolean isRegistered;

    @Test
    @Parameters(method = "registerUserWithOneEmptyFieldParameters")
    @TestCaseName("Создание пользователя без {0}")
    @DisplayName("Регистрация с отсутствующими обязательными полями")
    @Description("Проверка обработки попытки регистрации без обязательных полей. Ожидается код ответа 403 (Forbidden) и success=false для каждого случая")
    public void registerUserWithOneEmptyField(UserGenerator.UserField emptyField) {
        registerData = UserGenerator.getRegistrationDataWithOneEmptyField(emptyField);
        ValidatableResponse responseRegister = User.registerUser(registerData);

        statusCode = responseRegister.extract().statusCode();
        isRegistered = responseRegister.extract().path("success");

        Assert.assertEquals("Ошибка в коде или теле ответа", List.of(SC_FORBIDDEN, false), List.of(statusCode, isRegistered));
    }

    private Object[][] registerUserWithOneEmptyFieldParameters() {
        return new Object[][]{
                {UserGenerator.UserField.EMAIL},
                {UserGenerator.UserField.PASSWORD},
                {UserGenerator.UserField.NAME},
        };
    }
}
