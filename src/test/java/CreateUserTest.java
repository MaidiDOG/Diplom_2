import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserStep;
import type.User;
import type.UserCredential;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CreateUserTest {
    User user;
    public String accessToken;

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
    @DisplayName("Успешная регистрация пользователя")
    public void successfullyUserCreationTest() {
        ValidatableResponse regResponse = UserStep.createUser(user);
        ValidatableResponse loginResponse = UserStep.loginUser(UserCredential.getCredentials(user));
        accessToken = loginResponse.extract().path("accessToken").toString();
        regResponse
                .statusCode(200)
                .assertThat().body("success", is(true));
    }
    @Test
    @DisplayName("Регистрация пользователя с одинаковым email")
    public void duplicateCourierCreationTest(){
        UserStep.createUser(user);
        accessToken = UserStep.loginUser(UserCredential.getCredentials(user))
                .extract().path("accessToken").toString();
        ValidatableResponse response = UserStep.createUser(user);
        response
                .statusCode(403)
                .assertThat()
                .body("success",equalTo(false))
                .and()
                .body("message",equalTo("User already exists"));
    }
    @Test
    @DisplayName("Регистрация пользователя без email")
    public void courierCreationWithoutLoginTest(){
        user.setEmail(null);
        ValidatableResponse response = UserStep.createUser(user);
        response
                .statusCode(403)
                .assertThat()
                .body("success",equalTo(false))
                .and()
                .body("message",equalTo("Email, password and name are required fields"));
    }
}
