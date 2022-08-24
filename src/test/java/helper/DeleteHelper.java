package helper;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.Courier;
import pojo.DeleteCourierId;

import static org.hamcrest.Matchers.equalTo;

public class DeleteHelper {
    private Response response;

    @Step("Удаление курьера")
    public void deleteCourier(Courier courier) {
        /**
         * Метод получает id курьера и удаляет курьера
         */
        RequestCustom requestCustom = new RequestCustom();
        response = requestCustom.postLoginCourier(courier);
        //Получаем id
        int idCourier = response.then().extract().body().jsonPath().getInt("id");
        String pathId =String.valueOf(idCourier);
        //Удаляем курьера
        DeleteCourierId deleteCourierId = new DeleteCourierId(pathId);
        response = requestCustom.deleteCourier(deleteCourierId,pathId);
        response.then().assertThat().body("ok",equalTo(true));
    }

    @Step("Удаление курьера по id = {id}")
    public void deleteCourier(int id) {
        RequestCustom requestCustom = new RequestCustom();
        String pathId = String.valueOf(id);
        //Удаляем курьера
        DeleteCourierId deleteCourierId = new DeleteCourierId(pathId);
        response = requestCustom.deleteCourier(deleteCourierId, pathId);
        response.then().assertThat().body("ok", equalTo(true));
    }
}
