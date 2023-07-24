package config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Config {

    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    public static final String USER_URL = "/api/auth/";
    public static final String ORDERS_URL = "/api/orders/";
    public static final String INGREDIENTS_URL = "/api/ingredients";
    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
    }
}