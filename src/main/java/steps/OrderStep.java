package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import type.Order;

import static config.Config.*;
import static io.restassured.RestAssured.given;

public class OrderStep {

    @Step("Получение данных об ингредиентах")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getRequestSpec())
                .when()
                .get(INGREDIENTS_URL)
                .then().log().all();
    }

    @Step("Получение данных о заказах конкретного пользователя")
    public ValidatableResponse getOrderDataUser(String accessToken) {
        return given()
                .spec(getRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS_URL)
                .then().log().all();
    }
    @Step("Получение данных о заказах конкретного пользователя")
        public ValidatableResponse getOrderDataUserWithoutLogin() {
        return given()
                .spec(getRequestSpec())
                .when()
                .get(ORDERS_URL)
                .then().log().all();
    }

    @Step("Создание нового заказа без авторизации")
    public ValidatableResponse createNewOrder(Order order) {
        return given()
                .spec(getRequestSpec())
                .body(order)
                .when()
                .post(ORDERS_URL)
                .then().log().all();
    }

    @Step("Создание нового заказа с авторизацией")
    public ValidatableResponse createOrderWithLogin(String accessToken, Order order) {
        return given()
                .spec(getRequestSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS_URL)
                .then().log().all();
    }

    @Step("Создание нового заказа с авторизацией")
    public ValidatableResponse createOrderWithInvalidHash(String accessToken) {
        return given()
                .spec(getRequestSpec())
                .header("Authorization", accessToken)
                .body("{\"ingredients\": \"invalid ingredient hash\"}")
                .when()
                .post(ORDERS_URL)
                .then().log().all();
    }
}
