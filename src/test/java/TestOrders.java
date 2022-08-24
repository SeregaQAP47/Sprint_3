import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestOrders {
    private final String endpoint = "/api/v1/orders";
    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Запрос GET /api/v1/orders")
    @Description("Тестирование запроса GET /api/v1/orders" +
            "<ul> Проверяем: </ul>" +
            "<li> Статус код 200 </li>" +
            "<li> Список заказов не пустой в теле ответа </li>")
    public void testGetListOrders() {
        step(("Отправить запрос GET /api/v1/orders"), () -> {
            response = given()
                    .header("Content-type", "application/json")
                    .when()
                    .get(endpoint);
        });
        step(("Получили ответ"), () -> {
            System.out.println(response.asPrettyString());
            checkStatusCode(HttpStatus.SC_OK);
            checkListIsNotEmpty();
        });
    }

    @Step("Проверяем, что статус код  = {httpStatus}")
    private void checkStatusCode(int httpStatus) {
        assertThat(response.getStatusCode(), equalTo(httpStatus));
    }

    @Step
    private void checkListIsNotEmpty() {
        List<Object> orders = response.then().extract().body().jsonPath().getList("orders");
        assert !orders.isEmpty();
    }
}
