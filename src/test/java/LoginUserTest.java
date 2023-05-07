import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserStep;
import type.User;
import type.UserCredential;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    private User user;
    private String accessToken;


    @Before
    public void setup() {
        user = User.generateUserRandom();
    }
    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserStep.delete(accessToken);
        }
    }
    @Test
    @DisplayName("Успешный вход в системе")
    public void successfullyLoginTest() {
        UserStep.createUser(user);
        ValidatableResponse response = UserStep.loginUser(UserCredential.getCredentials(user));
        accessToken = response.extract().path("accessToken");
        response
                .statusCode(200)
                .assertThat()
                .body("accessToken", is(notNullValue()));
    }
    @Test
    @DisplayName("Авторизация пользователя c неверным логином")
    public void loginWithInvalidLoginTest() {
        user.setEmail("randomUnknownUser");
        ValidatableResponse response = UserStep.loginUser(UserCredential.getCredentials(user));
        response
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}
