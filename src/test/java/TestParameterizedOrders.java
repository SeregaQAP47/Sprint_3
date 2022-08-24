import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.ModelOrder;

import java.util.ArrayList;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class TestParameterizedOrders {
    private final String endpoint = "/api/v1/orders";
    private Response response;
    private final String color1;
    private final String color2;
    private ModelOrder order;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Parameterized.Parameters
    public static Object[] getDataColors() {
        return new Object[][]{
                {"BLACK", "GREY"},
                {"BLACK", ""},
                {"GREY", ""},
                {"", ""},
        };
    }

    public TestParameterizedOrders(String color1, String color2) {
        this.color1 = color1;
        this.color2 = color2;
    }

    @Test
    @DisplayName("Запрос POST /api/v1/orders")
    @Description("Тестирование запроса POST /api/v1/orders, создание заказа" +
            "<ul> Проверяем: </ul>" +
            "<li> Статус код 201</li>" +
            "<li> Заказ создается с разными параметрами цвета</li>")
    public void testCreateOrder() {
        step(("Генерируем тело запроса"), () -> {
            List<String> colors = new ArrayList<>();
            colors.add(color1);
            colors.add(color2);
            order = new ModelOrder
                    ("Иван",
                            "Иванов",
                            "Konoha, 142 apt.",
                            4,
                            "+7 800 355 35 40",
                            7,
                            "2022-08-17",
                            "comment",
                            colors);
        });
        step(("Отправить запрос POST /api/v1/orders"), () -> {
            response = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(order)
                    .when()
                    .post(endpoint);
        });
        step(("Получили ответ"), () -> {
            System.out.println(response.asPrettyString());
            checkStatusCode(HttpStatus.SC_CREATED);
            response.then().assertThat().body("track", notNullValue());
        });
    }


    @Step("Проверяем, что статус код  = {httpStatus}")
    private void checkStatusCode(int httpStatus) {
        assertThat(response.getStatusCode(), equalTo(httpStatus));
    }
}
