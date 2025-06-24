package site.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class Order extends ApiCore {

    private final static String ORDERS_PATH = "api/orders";

    @Step("Создание нового заказа")
    public static ValidatableResponse createOrder(site.stellarburgers.pojo.Order data, String bearerPlusToken) {
        return given()
                .spec(requestConfig(bearerPlusToken))
                .body(data)
                .when()
                .post(ORDERS_PATH)
                .then();
    }

    @Step("Получение всех заказов пользователя")
    public static ValidatableResponse getUserOrders(String bearerPlusToken) {
        return given()
                .spec(requestConfig(bearerPlusToken))
                .when()
                .get(ORDERS_PATH)
                .then();
    }
}