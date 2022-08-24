import helper.DeleteHelper;
import helper.RequestCustom;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.annotation.JacksonInject;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import pojo.Courier;
import pojo.DeleteCourierId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestCreatingCourier {
    private Response response;
    private DeleteHelper deleteHelper = new DeleteHelper();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Тестирование запроса POST/api/v1/courier" +
            "<p> Создание нового курьера запросом POST/api/v1/courier </p>" +
            "<ul> Проверяем: </ul>" +
            "<li> Статус код 201</li>" +
            "<li> JSON с корректной структурой в теле ответа </li>")
    public void testCreateCourier() {
        Courier courier = new Courier("Run2", "1234", "saske");
        RequestCustom requestCustom = new RequestCustom();
        response = requestCustom.postCreateCourierRequest(courier);
        System.out.println(response.asPrettyString());
        checkStatusCode(HttpStatus.SC_CREATED);
        checkOkTrue(response);

        deleteHelper.deleteCourier(courier);
    }

    @Test
    @DisplayName("Создание копии зарегистрированного курьера")
    @Description("Тестирование запроса POST/api/v1/courier" +
            "<p> Создание копии курьера невозможно" +
            "<ul> Проверяем: </ul>" +
            "<li> Статус код 409</li>" +
            "<li> JSON с корректной структурой в теле ответа </li>")
    public void testDuplicateCourier() {
        //Тест для проверки создания одинаковых курьеров
        Courier courier = new Courier("Миньон11", "1234", "Вася");
        //Запрос на создание курьера
        RequestCustom requestCustom = new RequestCustom();
        response = requestCustom.postCreateCourierRequest(courier);
        System.out.println(response.asPrettyString());
        checkStatusCode(HttpStatus.SC_CREATED);
        //Запрос на создание дубля курьера
        response = requestCustom.postCreateCourierRequest(courier);
        System.out.println(response.asPrettyString());
        checkStatusCode(HttpStatus.SC_CONFLICT);
        checkError(409, "Этот логин уже используется. Попробуйте другой.");
        deleteHelper.deleteCourier(courier);

    }

    @Step("Проверяем ответ с сообщением о ошибке")
    private void checkError(int expectCode, String expectMessage) {
        response.then().assertThat().body("code", equalTo(expectCode))
                .and().assertThat().body("message", equalTo(expectMessage));
    }

    @Step("Проверяем ответ")
    private void checkOkTrue(Response response) {
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Step("Проверяем, что статус код  = {httpStatus}")
    private void checkStatusCode(int httpStatus) {
        assertThat(response.getStatusCode(), equalTo(httpStatus));
    }


}
