import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderStep;
import steps.UserStep;
import type.Order;
import type.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class GettingOrdersUserTest {
    private User user;
    private Order order;
    private String accessToken;
    OrderStep orderStep = new OrderStep();
    UserStep userStep;

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
    @DisplayName("Получить список заказв с авторизацией")
    public void getDataOrderWithLoginTest() {
        ValidatableResponse userResponse = UserStep.createUser(user);
        ValidatableResponse ingredientsResponse = orderStep.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredientsResponse.extract().path("data[1]._id"));
        order = new Order(ingredients);
        accessToken = userResponse.extract().path("accessToken");
        orderStep.createOrderWithLogin(accessToken, order);
        ValidatableResponse orderResponse = orderStep.getOrderDataUser(accessToken);
        orderResponse
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получить список заказв без авторизацией")
    public void getDataOrderWithoutLoginTest() {
        ValidatableResponse ingredientsResponse = orderStep.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredientsResponse.extract().path("data[3]._id"));
        order = new Order(ingredients);
        orderStep.createNewOrder(order);
        ValidatableResponse orderResponse = orderStep.getOrderDataUserWithoutLogin();
        orderResponse
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message",equalTo("You should be authorised"));
    }
}
