package site.stellarburgers;

import io.restassured.response.ValidatableResponse;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import site.stellarburgers.client.User;
import site.stellarburgers.generator.OrderGenerator;
import site.stellarburgers.generator.UserGenerator;
import site.stellarburgers.pojo.Order;
import site.stellarburgers.pojo.RegisterUser;

import java.util.List;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_OK;

@RunWith(JUnitParamsRunner.class)
public class CreateOrderTest {

    private final static RegisterUser registerData = UserGenerator.getDefaultRegistrationData();
    private static String token = "";
    Order orderData;
    private int statusCode;
    private boolean isCreated;

    @BeforeClass
    public static void beforeAll() {
        ValidatableResponse responseRegister = User.registerUser(registerData);
        token = responseRegister.extract().path("accessToken");
    }

    @AfterClass
    public static void afterAll() {
        ValidatableResponse responseDelete = User.deleteUser(token);
    }

    @Test
    @Parameters(method = "createOrderParameters")
    @TestCaseName("Создание заказа с авторизацией ({0}) и непустым ({1}) корректным ({2}) списком ингредиентов")
    public void createOrder(boolean isAuth, boolean haveIngredients, boolean isHashCorrect, int status) {
        String token2 = "abc";
        if (isAuth) {
            token2 = token;
        }
        if (isHashCorrect) {
            if (haveIngredients) {
                orderData = OrderGenerator.getDefaultOrder();
            } else {
                orderData = OrderGenerator.getOrderWithoutIngredients();
            }
        } else {
            orderData = OrderGenerator.getOrderWithIncorrectHash();
        }
        ValidatableResponse responseCreateOrder = site.stellarburgers.client.Order.createOrder(orderData, token2);

        statusCode = responseCreateOrder.extract().statusCode();
        if (isHashCorrect) {
            isCreated = responseCreateOrder.extract().path("success");
        } else {
            isCreated = false;
        }

        Assert.assertEquals("Ошибка в коде или теле ответа",
                List.of(status, haveIngredients & isHashCorrect),
                List.of(statusCode, isCreated));
    }

    private Object[][] createOrderParameters() {
        return new Object[][]{
                {true, true, true, SC_OK},
                {true, true, false, SC_INTERNAL_SERVER_ERROR},
                {true, false, true, SC_BAD_REQUEST},
                {false, true, true, SC_OK},
        };
    }
}
