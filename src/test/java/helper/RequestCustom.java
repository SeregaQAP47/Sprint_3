package helper;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.Courier;
import pojo.DeleteCourierId;

import static io.restassured.RestAssured.given;

public class RequestCustom {
    private Response response;

    @Step("Запрос POST /api/v1/courier для создание курьера")
    public Response postCreateCourierRequest(Courier courier) {
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Запрос POST /api/v1/courier/login для получение id курьера")
    public Response postLoginCourier(Courier courier) {
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        return this.response;
    }

    @Step("Запрос DELETE /api/v1/courier/")
    public Response deleteCourier(DeleteCourierId bodyRequest,String id) {
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(bodyRequest)
                .when()
                .delete("/api/v1/courier/" + id);
        return this.response;
    }

}
