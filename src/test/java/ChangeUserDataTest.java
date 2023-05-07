import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserStep;
import type.User;
import static org.hamcrest.Matchers.*;

public class ChangeUserDataTest {
    User user;
    private String accessToken;
    @Before
    public void setUp() {
        user = User.generateUserRandom();
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserStep.delete(accessToken);
        }
    }
    @Test
    @DisplayName("Изменение данных пользователя, прошедшего авторизацию")
    public void changeUserDataWithALoginTest() {
        ValidatableResponse regResponse = UserStep.createUser(user);
        accessToken = regResponse.extract().path("accessToken");
        ValidatableResponse changeResponse = UserStep.changeDataWithAuth(user, accessToken);
        changeResponse
                .statusCode(200)
                .assertThat()
                .body("user.email", equalTo("superemailfortest@gmail.com"))
                .and()
                .body("user.name", equalTo("superNameForTest"))
                .and()
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void changeUserDataWithoutLoginTest() {
        String newEmail = user.getEmail() + "testEmail";
        user.setName(newEmail);
        ValidatableResponse responseUpdate = UserStep.changeDataWithoutAuth(user);
        responseUpdate
                .statusCode(401)
                .assertThat()
                .body("success",equalTo(false))
                .and()
                .body("message",equalTo("You should be authorised"));
    }

}
