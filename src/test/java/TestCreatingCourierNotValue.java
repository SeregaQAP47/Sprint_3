import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.Courier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestCreatingCourierNotValue {
    private final String endpointCourier = "/api/v1/courier";
    private Response response;
    private final String login;
    private final String password;
    private final String firstName = "Вася";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    public TestCreatingCourierNotValue(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[] getData() {
        return new Object[][]{
                {"", "12345"},
                {"Миньон2", ""},
                {"", ""},
        };
    }

    @Test
    public void testCreateCourierWithoutRequiredParameters() {
        Courier courier = new Courier(login, password, firstName);
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(endpointCourier);

        System.out.println(response.asPrettyString());
        checkStatusCode(HttpStatus.SC_BAD_REQUEST);
        checkMessageError(400, "Недостаточно данных для создания учетной записи");
    }


    @Step("Проверяем ответ с сообщение о ошибке")
    private void checkMessageError(int expectCode, String expectMessage) {
        response.then().assertThat().body("code", equalTo(expectCode))
                .and().assertThat().body("message", equalTo(expectMessage));

    }

    @Step("Проверяем, что статус код  = {httpStatus}")
    private void checkStatusCode(int httpStatus) {
        assertThat(response.getStatusCode(), equalTo(httpStatus));
    }
}
