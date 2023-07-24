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

public class CreateOrderTest {
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
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithIngredientsTest() {
        ValidatableResponse userResponse = UserStep.createUser(user);
        ValidatableResponse ingredientsResponse = orderStep.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredientsResponse.extract().path("data[1]._id"));
        order = new Order(ingredients);
        accessToken = userResponse.extract().path("accessToken");
        ValidatableResponse orderResponse = orderStep.createOrderWithLogin(accessToken, order);
        orderResponse
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutLoginTest() {
        ValidatableResponse ingredientsResponse = orderStep.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredientsResponse.extract().path("data[0]._id"));
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderStep.createNewOrder(order);
        orderResponse
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        ValidatableResponse userResponse = UserStep.createUser(user);
        List<String> ingredients = new ArrayList<>();
        order = new Order(ingredients);
        accessToken = userResponse.extract().path("accessToken");
        ValidatableResponse orderResponse = orderStep.createOrderWithLogin(accessToken, order);
        orderResponse
                .statusCode(400)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message",equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неправильным хешем ингредиента")
    public void createOrderWithBrokenIngredientsTest() {
        ValidatableResponse userResponse = UserStep.createUser(user);
        accessToken = userResponse.extract().path("accessToken");
        ValidatableResponse orderResponse = orderStep.createOrderWithInvalidHash(accessToken);
        orderResponse
                .statusCode(500);
    }
}


