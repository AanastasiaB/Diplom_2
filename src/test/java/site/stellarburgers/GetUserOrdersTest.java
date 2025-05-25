package site.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import junitparams.JUnitParamsRunner;
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

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

@RunWith(JUnitParamsRunner.class)
public class GetUserOrdersTest {

    private final static RegisterUser registerData = UserGenerator.getDefaultRegistrationData();
    private final static Order ORDER_DATA = OrderGenerator.getDefaultOrder();
    private static String token = "";
    private int statusCode;
    private boolean isGot;

    @BeforeClass
    public static void setUp() {
        ValidatableResponse responseRegister = User.registerUser(registerData);
        token = responseRegister.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = site.stellarburgers.client.Order.createOrder(ORDER_DATA, token);
    }

    @AfterClass
    public static void tearDown() {
        ValidatableResponse responseDelete = User.deleteUser(token);
    }

    @Test
    @DisplayName("Получение заказов конкретного авторизованного пользователя")
    public void getAuthorizedUserOrders() {
        ValidatableResponse responseGetOrders = site.stellarburgers.client.Order.getUserOrders(token);
        statusCode = responseGetOrders.extract().statusCode();
        isGot = responseGetOrders.extract().path("success");
        List<Object> orders = responseGetOrders.extract().path("orders");

        Assert.assertEquals("Ошибка в коде или теле ответа", List.of(SC_OK, true, false),
                List.of(statusCode, isGot, orders.isEmpty()));
    }

    @Test
    @DisplayName("Получение заказов конкретного неавторизованного пользователя")
    public void getUnauthorizedUserOrders() {
        ValidatableResponse responseGetOrders = site.stellarburgers.client.Order.getUserOrders("abc");
        statusCode = responseGetOrders.extract().statusCode();
        isGot = responseGetOrders.extract().path("success");

        Assert.assertEquals("Ошибка в коде или теле ответа", List.of(SC_UNAUTHORIZED, false),
                List.of(statusCode, isGot));
    }
}
