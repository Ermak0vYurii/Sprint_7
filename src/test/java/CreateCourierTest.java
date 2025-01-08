import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;


public class CreateCourierTest extends BaseHttpClient {

    Courier courier = new Courier("test2025", "1234", "ivan");

    @Test
    @DisplayName("Курьера можно создать")
    @Description("Запрос возвращает правильный код ответа")
    public void canCreateCourierTest() {
    Response response = createCourier(courier);
    compareStatusCode(response, 201);
    deleteCourier();
    }

    @Test
    @DisplayName("Успешный запрос возвращает ОК:true")
    public void successfulRequestReturnOkTrueTest() {
    Response response = createCourier(courier);
    compareResponseBody(response);
    deleteCourier();
    }

    @Test
    @DisplayName("Если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void cantCreateCourierSameLoginTest() {
    Courier courierSameLogin = new Courier(courier.getLogin(), "5678", "piter");
    createCourier(courier);
    Response response = createCourier(courierSameLogin);
    compareStatusCode(response, 409);
    compareResponseBodyMessage(response, "Этот логин уже используется"); // баг
    deleteCourier();
    }

    @Test
    @DisplayName("Невозможно создать курьера без обязательного поля login")
    public void cantCreateCourierWithoutloginTest() {
        Courier courierWithoutLogin = new Courier("", courier.getPassword(), courier.getFirstName());
        Response response = createCourier(courierWithoutLogin);
        compareStatusCode(response, 400);
        compareResponseBodyMessage(response, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Невозможно создать курьера без обязательного поля password")
    public void cantCreateCourierWithoutPasswordTest() {
        Courier courierWithoutPassword = new Courier(courier.getLogin(), "", courier.getFirstName());
        Response response = createCourier(courierWithoutPassword);
        compareStatusCode(response, 400);
        compareResponseBodyMessage(response, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Невозможно создать двух одинаковых курьеров")
    public void impossibleCreateTwoIdenticalCouriersTest() {
        Courier secondIdenticalCourier = new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName());
        createCourier(courier);
        Response response = createCourier(secondIdenticalCourier);
        compareStatusCode(response, 409);
        deleteCourier();
    }

    @Step("Send POST request to api/v1/courier")
    public Response createCourier(Object courier) {
        return doPostRequest("api/v1/courier", courier);
    }

    @Step("Send DELETE request to api/v1/courier/:id")
    public void deleteCourier(){
        LoginCourier loginCourier = new LoginCourier(courier.getLogin(), courier.getPassword());
        Response response = doPostRequest("api/v1/courier/login", loginCourier);
        int id = response.then().extract().body().path("id");
        doDeleteRequest("api/v1/courier/" + id);
    }

    @Step("Compare status code")
    public void compareStatusCode(Response response, int code){
        response.then().statusCode(code);
    }

    @Step("Compare response body")
    public void compareResponseBody(Response response){
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Step("Compare body message")
    public void compareResponseBodyMessage(Response response, String message) {
        response.then().assertThat().body("message", equalTo(message));
    }
}
