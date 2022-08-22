import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import pojo.Courier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestLoginCourier {
    private final String endpoint = "/api/v1/courier/login";
    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void testLoginCourier() {
        Courier courier = new Courier();
        courier.setLogin("Миньон1");
        courier.setPassword("1234");

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(endpoint);
        System.out.println(response.asPrettyString());
        checkStatusCode(HttpStatus.SC_OK);
        response.then().assertThat().body("id", notNullValue());
    }

    @Test
    public void testNegativePasswordCourier() {
        //Тест проверяет запрос с не правильным паролем
        Courier courier = new Courier();
        courier.setLogin("Миньон1");
        courier.setPassword("0000");

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(endpoint);
        System.out.println(response.asPrettyString());
        checkStatusCode(HttpStatus.SC_NOT_FOUND);
        checkError(404, "Учетная запись не найдена");
    }

    @Step("Проверяем, что статус код  = {httpStatus}")
    private void checkStatusCode(int httpStatus) {
        assertThat(response.getStatusCode(), equalTo(httpStatus));
    }

    @Step("Проверяем ответ с сообщением о ошибке")
    private void checkError(int expectCode, String expectMessage) {
        response.then().assertThat().body("code", equalTo(expectCode))
                .and().assertThat().body("message", equalTo(expectMessage));
    }

}
