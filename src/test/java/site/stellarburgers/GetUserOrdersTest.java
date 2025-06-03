package site.stellarburgers;

import io.qameta.allure.Description;
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
    @DisplayName("ÐÐ¾Ð»ÑÑÐµÐ½Ð¸Ðµ Ð·Ð°ÐºÐ°Ð·Ð¾Ð² Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð¾Ð²Ð°Ð½Ð½Ð¾Ð³Ð¾ Ð¿Ð¾Ð»ÑÐ·Ð¾Ð²Ð°ÑÐµÐ»Ñ")
    @Description("ÐÑÐ¾Ð²ÐµÑÐºÐ° ÑÑÐ¿ÐµÑÐ½Ð¾Ð³Ð¾ Ð¿Ð¾Ð»ÑÑÐµÐ½Ð¸Ñ ÑÐ¿Ð¸ÑÐºÐ° Ð·Ð°ÐºÐ°Ð·Ð¾Ð² Ð´Ð»Ñ Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð¾Ð²Ð°Ð½Ð½Ð¾Ð³Ð¾ Ð¿Ð¾Ð»ÑÐ·Ð¾Ð²Ð°ÑÐµÐ»Ñ. " +
            "ÐÐ¶Ð¸Ð´Ð°ÐµÑÑÑ ÐºÐ¾Ð´ Ð¾ÑÐ²ÐµÑÐ° 200, success=true Ð¸ Ð½ÐµÐ¿ÑÑÑÐ¾Ð¹ ÑÐ¿Ð¸ÑÐ¾Ðº Ð·Ð°ÐºÐ°Ð·Ð¾Ð²")
    public void getAuthorizedUserOrders() {
        ValidatableResponse responseGetOrders = site.stellarburgers.client.Order.getUserOrders(token);
        statusCode = responseGetOrders.extract().statusCode();
        isGot = responseGetOrders.extract().path("success");
        List<Object> orders = responseGetOrders.extract().path("orders");

        Assert.assertEquals("ÐÑÐ¸Ð±ÐºÐ° Ð² ÐºÐ¾Ð´Ðµ Ð¸Ð»Ð¸ ÑÐµÐ»Ðµ Ð¾ÑÐ²ÐµÑÐ°", List.of(SC_OK, true, false),
                List.of(statusCode, isGot, orders.isEmpty()));
    }

    @Test
    @DisplayName("ÐÐ¾Ð»ÑÑÐµÐ½Ð¸Ðµ Ð·Ð°ÐºÐ°Ð·Ð¾Ð² Ð½ÐµÐ°Ð²ÑÐ¾ÑÐ¸Ð·Ð¾Ð²Ð°Ð½Ð½Ð¾Ð³Ð¾ Ð¿Ð¾Ð»ÑÐ·Ð¾Ð²Ð°ÑÐµÐ»Ñ")
    @Description("ÐÑÐ¾Ð²ÐµÑÐºÐ° Ð¿Ð¾Ð¿ÑÑÐºÐ¸ Ð¿Ð¾Ð»ÑÑÐµÐ½Ð¸Ñ ÑÐ¿Ð¸ÑÐºÐ° Ð·Ð°ÐºÐ°Ð·Ð¾Ð² Ð±ÐµÐ· Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð°ÑÐ¸Ð¸. ÐÐ¶Ð¸Ð´Ð°ÐµÑÑÑ ÐºÐ¾Ð´ Ð¾ÑÐ²ÐµÑÐ° 401 (Unauthorized) Ð¸ success=false")
    public void getUnauthorizedUserOrders() {
        ValidatableResponse responseGetOrders = site.stellarburgers.client.Order.getUserOrders("abc");
        statusCode = responseGetOrders.extract().statusCode();
        isGot = responseGetOrders.extract().path("success");

        Assert.assertEquals("ÐÑÐ¸Ð±ÐºÐ° Ð² ÐºÐ¾Ð´Ðµ Ð¸Ð»Ð¸ ÑÐµÐ»Ðµ Ð¾ÑÐ²ÐµÑÐ°", List.of(SC_UNAUTHORIZED, false),
                List.of(statusCode, isGot));
    }
}

