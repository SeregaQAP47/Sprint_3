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

public class TestCreatingCourier {
    private final String endpointCourier = "/api/v1/courier";
    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void testCreateCourier() {
        Courier courier = new Courier("Runner", "1234", "saske");
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(endpointCourier);

        System.out.println(response.asPrettyString());
        checkStatusCode(HttpStatus.SC_CREATED);
        checkOkTrue(response);
    }

    @Test
    public void testDuplicateCourier() {
        //Тест для проверки создания одинаковых курьеров
        Courier courier = new Courier("Миньон1","1234","Вася");
        //Запрос на создание курьера
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(endpointCourier);

        System.out.println(response.asPrettyString());
        checkStatusCode(HttpStatus.SC_CREATED);
        //Запрос на создание дубля курьера
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(endpointCourier);
        System.out.println(response.asPrettyString());

        checkStatusCode(HttpStatus.SC_CONFLICT);
        checkError(409,"Этот логин уже используется. Попробуйте другой.");

    }

    @Step("Проверяем ответ с сообщением о ошибке")
    private void checkError(int expectCode , String expectMessage) {
        response.then().assertThat().body("code",equalTo(expectCode))
                .and().assertThat().body("message",equalTo(expectMessage));
    }

    @Step("Проверяем ответ")
    private void checkOkTrue(Response response) {
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Step("Проверяем, что статус код  = {httpStatus}")
    private void checkStatusCode(int httpStatus) {
        assertThat(response.getStatusCode(),equalTo(httpStatus));
    }
}
