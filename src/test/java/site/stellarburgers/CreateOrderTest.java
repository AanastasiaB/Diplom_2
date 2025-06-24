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
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Проверка успешного создания заказа для авторизованного пользователя с корректным списком ингредиентов. " +
            "Ожидается код ответа 200 и success=true")
    public void createOrderWithAuthAndIngredientsShouldSucceed() {
        createTestUser();
        orderData = OrderGenerator.getDefaultOrder();
        ValidatableResponse response = site.stellarburgers.client.Order.createOrder(orderData, token);

        assertEquals(SC_OK, response.extract().statusCode());
        assertTrue(response.extract().path("success"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хэшем ингредиентов")
    @Description("Проверка обработки ошибки при создании заказа с некорректным хэшем ингредиентов. " +
            "Ожидается код ответа 500 и сообщение об ошибке")
    public void createOrderWithAuthAndInvalidHashShouldFail() {
        createTestUser();
        orderData = OrderGenerator.getOrderWithIncorrectHash();
        ValidatableResponse response = site.stellarburgers.client.Order.createOrder(orderData, token);

        assertEquals(SC_INTERNAL_SERVER_ERROR, response.extract().statusCode());
        assertEquals("HTTP/1.1 500 Internal Server Error", response.extract().statusLine());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка обработки ошибки при попытке создания заказа без указания ингредиентов. " +
            "Ожидается код ответа 400 и соответствующее сообщение об ошибке")
    public void createOrderWithAuthAndNoIngredientsShouldFail() {
        createTestUser();
        orderData = OrderGenerator.getOrderWithoutIngredients();
        ValidatableResponse response = site.stellarburgers.client.Order.createOrder(orderData, token);

        assertEquals(SC_BAD_REQUEST, response.extract().statusCode());
        assertEquals("Ingredient ids must be provided", response.extract().path("message"));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка создания заказа без токена авторизации. Ожидается код ответа 200 и success=true (если API позволяет создавать заказы без авторизации)")
    public void createOrderWithoutAuthWithIngredientsShouldSucceed() {
        orderData = OrderGenerator.getDefaultOrder();
        ValidatableResponse response = site.stellarburgers.client.Order.createOrder(orderData, "");

        assertEquals(SC_OK, response.extract().statusCode());
        assertTrue(response.extract().path("success"));
    }
}
