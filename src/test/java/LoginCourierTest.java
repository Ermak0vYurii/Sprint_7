import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

//    курьер может авторизоваться;+
//    для авторизации нужно передать все обязательные поля;+
//    система вернёт ошибку, если неправильно указать логин или пароль;+
//    если какого-то поля нет, запрос возвращает ошибку;+
//    если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;+
//    успешный запрос возвращает id.+

public class LoginCourierTest extends BaseHttpClient {

    Courier courier = new Courier("test2025", "1234", "ivan");
    LoginCourier loginCourier = new LoginCourier(courier.getLogin(), courier.getPassword());

    @Step("Send POST request to api/v1/courier")
    public void createCourier() {
        doPostRequest("api/v1/courier", courier);
    }
    @Step("Send DELETE request to api/v1/courier/:id")
    public void deleteCourier() {
        int id = doPostRequest("api/v1/courier/login", loginCourier)
                .then().extract().body().path("id");
        doDeleteRequest("api/v1/courier/" + id);
    }
    @Step("Send POST request to api/v1/courier/login")
    public Response loginCourier() {
       return doPostRequest("api/v1/courier/login", loginCourier);
    }
    @Step("Compare status code")
    public void compareStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }
    @Step("Compare body message")
    public void compareResponseBodyMessage(Response response, String message) {
        response.then().assertThat().body("message", equalTo(message));
    }
    @Step("Response body login have id field")
    public void responseBodyLoginHaveId(Response response) {
        response.then().assertThat().body("id", notNullValue());
    }

    @Test
    public void successfulAuthCourierTest() {
        createCourier();
        Response response = loginCourier();
        compareStatusCode(response, 200);
        deleteCourier();
    }

    @Test
    public void successfulRequestReturnIdTest() {
        createCourier();
        Response response = loginCourier();
        responseBodyLoginHaveId(response);
        deleteCourier();
    }

    @Test
    public void authNonExistentCourierTest() {
        Response response = loginCourier();
        compareStatusCode(response, 404);
        compareResponseBodyMessage(response, "Учетная запись не найдена");
    }

    @Test
    public void cantAuthCourierWithoutloginTest() {
        createCourier();
        FailLoginCourier courierWithoutLogin = new FailLoginCourier(courier.getPassword());
        Response response = doPostRequest("api/v1/courier/login", courierWithoutLogin);
        compareStatusCode(response, 400);
        compareResponseBodyMessage(response, "Недостаточно данных для входа");
        deleteCourier();

    }


}
