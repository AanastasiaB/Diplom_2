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
    @TestCaseName("ÐÐ·Ð¼ÐµÐ½ÐµÐ½Ð¸Ðµ Ð´Ð°Ð½Ð½ÑÑ Ð¿Ð¾Ð»ÑÐ·Ð¾Ð²Ð°ÑÐµÐ»Ñ Ñ Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð°ÑÐ¸ÐµÐ¹: {0}")
    @DisplayName("ÐÐ±Ð½Ð¾Ð²Ð»ÐµÐ½Ð¸Ðµ Ð´Ð°Ð½Ð½ÑÑ Ð¿Ð¾Ð»ÑÐ·Ð¾Ð²Ð°ÑÐµÐ»Ñ")
    @Description("ÐÑÐ¾Ð²ÐµÑÐºÐ° Ð¾Ð±Ð½Ð¾Ð²Ð»ÐµÐ½Ð¸Ñ Ð´Ð°Ð½Ð½ÑÑ Ð¿Ð¾Ð»ÑÐ·Ð¾Ð²Ð°ÑÐµÐ»Ñ Ñ Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð°ÑÐ¸ÐµÐ¹ Ð¸ Ð±ÐµÐ·. " +
            "ÐÐ¶Ð¸Ð´Ð°ÐµÑÑÑ ÑÑÐ¿ÐµÑÐ½Ð¾Ðµ Ð¾Ð±Ð½Ð¾Ð²Ð»ÐµÐ½Ð¸Ðµ Ð¿ÑÐ¸ Ð½Ð°Ð»Ð¸ÑÐ¸Ð¸ ÑÐ¾ÐºÐµÐ½Ð° (ÐºÐ¾Ð´ 200) " +
            "Ð¸ Ð¾ÑÐ¸Ð±ÐºÐ° Ð¿ÑÐ¸ Ð¾ÑÑÑÑÑÑÐ²Ð¸Ð¸ Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð°ÑÐ¸Ð¸ (ÐºÐ¾Ð´ 401)")
    public void updateUserWithAuthorization(boolean isAuth, int status) {
        String token2 = "abc";
        if (isAuth) {
            token2 = token;
        }
        ValidatableResponse responseUpdate = User.updateUser(updateData, token2);

        statusCode = responseUpdate.extract().statusCode();
        isUpdated = responseUpdate.extract().path("success");

        Assert.assertEquals("ÐÑÐ¸Ð±ÐºÐ° Ð² ÐºÐ¾Ð´Ðµ Ð¸Ð»Ð¸ ÑÐµÐ»Ðµ Ð¾ÑÐ²ÐµÑÐ°", List.of(status, isAuth), List.of(statusCode, isUpdated));
    }

    private Object[][] updateUserWithAuthorizationParameters() {
        return new Object[][]{
                {true, SC_OK},
                {false, SC_UNAUTHORIZED},
        };
    }
}
