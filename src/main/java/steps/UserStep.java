package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import type.User;
import type.UserCredential;

import static config.Config.*;
import static io.restassured.RestAssured.given;

public class UserStep {
    @Step("Регистрация пользователя")
    public static ValidatableResponse createUser(User user) {
        return given()
                .spec(getRequestSpec())
                .when()
                .body(user).log().all()
                .post(USER_URL + "register").then().log().all();
    }

    @Step("Авторизация пользователя")
    public static ValidatableResponse loginUser(UserCredential userCredential) {
        return given()
                .spec(getRequestSpec())
                .when()
                .body(userCredential)
                .post(USER_URL + "login").then().log().all();
    }
    @Step("Удаление пользователя")
    public static ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_URL + "user").then().log().all();

    }
    @Step("Изменения  пользователя")
    public static ValidatableResponse changeDataWithAuth(User user, String accessToken) {
        return given()
                .spec(getRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .body("{\"email\": \"superEmailForTest@gmail.com\",  \"name\": \"superNameForTest\"}")
                .patch(USER_URL + "user").then().log().all();
    }

    @Step("Ошибка изменений пользователя без авторизации")
    public static ValidatableResponse changeDataWithoutAuth(User user) {
        return given()
                .spec(getRequestSpec())
                .body(user)
                .patch(USER_URL + "user").then().log().all();
    }
}
