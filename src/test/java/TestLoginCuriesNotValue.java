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
public class TestLoginCuriesNotValue {
    private final String endpoint = "/api/v1/courier/login";
    private Response response;
    private final String login;
    private final String password;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    public TestLoginCuriesNotValue(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[] getData() {
        return new Object[][]{
                {"", "12345"},
                {"Миньон9", ""},
                {"", ""},
        };
    }

    @Test
    public void testNegativeParametersLoginCourier() {
        Courier courier = new Courier();
        courier.setLogin(login);
        courier.setPassword(password);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(endpoint);
        System.out.println(response.asPrettyString());
        checkStatusCode(HttpStatus.SC_BAD_REQUEST);
        checkError(400, "Недостаточно данных для входа");
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
