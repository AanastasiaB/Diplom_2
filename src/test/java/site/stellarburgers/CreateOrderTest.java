package site.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.stellarburgers.client.User;
import site.stellarburgers.generator.OrderGenerator;
import site.stellarburgers.generator.UserGenerator;
import site.stellarburgers.pojo.Order;
import site.stellarburgers.pojo.RegisterUser;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateOrderTest {

    private RegisterUser registerData;
    private String token;
    private Order orderData;

    @After
    public void tearDown() {
        if (token != null && !token.isEmpty()) {
            User.deleteUser(token);
            token = null;
        }
    }

    private void createTestUser() {
        registerData = UserGenerator.getDefaultRegistrationData();
        token = User.registerUser(registerData).extract().path("accessToken");
    }

    @Test
    @DisplayName("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ Ð·Ð°ÐºÐ°Ð·Ð° Ñ Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð°ÑÐ¸ÐµÐ¹ Ð¸ Ð¸Ð½Ð³ÑÐµÐ´Ð¸ÐµÐ½ÑÐ°Ð¼Ð¸")
    @Description("ÐÑÐ¾Ð²ÐµÑÐºÐ° ÑÑÐ¿ÐµÑÐ½Ð¾Ð³Ð¾ ÑÐ¾Ð·Ð´Ð°Ð½Ð¸Ñ Ð·Ð°ÐºÐ°Ð·Ð° Ð´Ð»Ñ Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð¾Ð²Ð°Ð½Ð½Ð¾Ð³Ð¾ Ð¿Ð¾Ð»ÑÐ·Ð¾Ð²Ð°ÑÐµÐ»Ñ Ñ ÐºÐ¾ÑÑÐµÐºÑÐ½ÑÐ¼ ÑÐ¿Ð¸ÑÐºÐ¾Ð¼ Ð¸Ð½Ð³ÑÐµÐ´Ð¸ÐµÐ½ÑÐ¾Ð². " +
            "ÐÐ¶Ð¸Ð´Ð°ÐµÑÑÑ ÐºÐ¾Ð´ Ð¾ÑÐ²ÐµÑÐ° 200 Ð¸ success=true")
    public void createOrderWithAuthAndIngredientsShouldSucceed() {
        createTestUser();
        orderData = OrderGenerator.getDefaultOrder();
        ValidatableResponse response = site.stellarburgers.client.Order.createOrder(orderData, token);

        assertEquals(SC_OK, response.extract().statusCode());
        assertTrue(response.extract().path("success"));
    }

    @Test
    @DisplayName("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ Ð·Ð°ÐºÐ°Ð·Ð° Ñ Ð½ÐµÐ²ÐµÑÐ½ÑÐ¼ ÑÑÑÐµÐ¼ Ð¸Ð½Ð³ÑÐµÐ´Ð¸ÐµÐ½ÑÐ¾Ð²")
    @Description("ÐÑÐ¾Ð²ÐµÑÐºÐ° Ð¾Ð±ÑÐ°Ð±Ð¾ÑÐºÐ¸ Ð¾ÑÐ¸Ð±ÐºÐ¸ Ð¿ÑÐ¸ ÑÐ¾Ð·Ð´Ð°Ð½Ð¸Ð¸ Ð·Ð°ÐºÐ°Ð·Ð° Ñ Ð½ÐµÐºÐ¾ÑÑÐµÐºÑÐ½ÑÐ¼ ÑÑÑÐµÐ¼ Ð¸Ð½Ð³ÑÐµÐ´Ð¸ÐµÐ½ÑÐ¾Ð². " +
            "ÐÐ¶Ð¸Ð´Ð°ÐµÑÑÑ ÐºÐ¾Ð´ Ð¾ÑÐ²ÐµÑÐ° 500 Ð¸ ÑÐ¾Ð¾Ð±ÑÐµÐ½Ð¸Ðµ Ð¾Ð± Ð¾ÑÐ¸Ð±ÐºÐµ")
    public void createOrderWithAuthAndInvalidHashShouldFail() {
        createTestUser();
        orderData = OrderGenerator.getOrderWithIncorrectHash();
        ValidatableResponse response = site.stellarburgers.client.Order.createOrder(orderData, token);

        assertEquals(SC_INTERNAL_SERVER_ERROR, response.extract().statusCode());
        assertEquals("HTTP/1.1 500 Internal Server Error", response.extract().statusLine());
    }

    @Test
    @DisplayName("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ Ð·Ð°ÐºÐ°Ð·Ð° Ð±ÐµÐ· Ð¸Ð½Ð³ÑÐµÐ´Ð¸ÐµÐ½ÑÐ¾Ð²")
    @Description("ÐÑÐ¾Ð²ÐµÑÐºÐ° Ð¾Ð±ÑÐ°Ð±Ð¾ÑÐºÐ¸ Ð¾ÑÐ¸Ð±ÐºÐ¸ Ð¿ÑÐ¸ Ð¿Ð¾Ð¿ÑÑÐºÐµ ÑÐ¾Ð·Ð´Ð°Ð½Ð¸Ñ Ð·Ð°ÐºÐ°Ð·Ð° Ð±ÐµÐ· ÑÐºÐ°Ð·Ð°Ð½Ð¸Ñ Ð¸Ð½Ð³ÑÐµÐ´Ð¸ÐµÐ½ÑÐ¾Ð². " +
            "ÐÐ¶Ð¸Ð´Ð°ÐµÑÑÑ ÐºÐ¾Ð´ Ð¾ÑÐ²ÐµÑÐ° 400 Ð¸ ÑÐ¾Ð¾ÑÐ²ÐµÑÑÑÐ²ÑÑÑÐµÐµ ÑÐ¾Ð¾Ð±ÑÐµÐ½Ð¸Ðµ Ð¾Ð± Ð¾ÑÐ¸Ð±ÐºÐµ")
    public void createOrderWithAuthAndNoIngredientsShouldFail() {
        createTestUser();
        orderData = OrderGenerator.getOrderWithoutIngredients();
        ValidatableResponse response = site.stellarburgers.client.Order.createOrder(orderData, token);

        assertEquals(SC_BAD_REQUEST, response.extract().statusCode());
        assertEquals("Ingredient ids must be provided", response.extract().path("message"));
    }

    @Test
    @DisplayName("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ Ð·Ð°ÐºÐ°Ð·Ð° Ð±ÐµÐ· Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð°ÑÐ¸Ð¸")
    @Description("ÐÑÐ¾Ð²ÐµÑÐºÐ° ÑÐ¾Ð·Ð´Ð°Ð½Ð¸Ñ Ð·Ð°ÐºÐ°Ð·Ð° Ð±ÐµÐ· ÑÐ¾ÐºÐµÐ½Ð° Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð°ÑÐ¸Ð¸. ÐÐ¶Ð¸Ð´Ð°ÐµÑÑÑ ÐºÐ¾Ð´ Ð¾ÑÐ²ÐµÑÐ° 200 Ð¸ success=true (ÐµÑÐ»Ð¸ API Ð¿Ð¾Ð·Ð²Ð¾Ð»ÑÐµÑ ÑÐ¾Ð·Ð´Ð°Ð²Ð°ÑÑ Ð·Ð°ÐºÐ°Ð·Ñ Ð±ÐµÐ· Ð°Ð²ÑÐ¾ÑÐ¸Ð·Ð°ÑÐ¸Ð¸)")
    public void createOrderWithoutAuthWithIngredientsShouldSucceed() {
        orderData = OrderGenerator.getDefaultOrder();
        ValidatableResponse response = site.stellarburgers.client.Order.createOrder(orderData, "");

        assertEquals(SC_OK, response.extract().statusCode());
        assertTrue(response.extract().path("success"));
    }
}
