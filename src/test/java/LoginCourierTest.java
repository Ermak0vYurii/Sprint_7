import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


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
    @DisplayName("Курьер может авторизоваться")
    public void successfulAuthCourierTest() {
        createCourier();
        Response response = loginCourier();
        compareStatusCode(response, 200);
        deleteCourier();
    }

    @Test
    @DisplayName("Успешный запрос возвращает id")
    public void successfulRequestReturnIdTest() {
        createCourier();
        Response response = loginCourier();
        responseBodyLoginHaveId(response);
        deleteCourier();
    }

    @Test
    @DisplayName("Авторизация несуществующего курьера возвращает ошибку")
    public void authNonExistentCourierTest() {
        Response response = loginCourier();
        compareStatusCode(response, 404);
        compareResponseBodyMessage(response, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Авторизация с неправильно указанным логином возвращает ошибку")
    public void cantAuthCourierFailLoginTest() {
        createCourier();
        Courier failLoginCourier = new Courier("test2052", courier.getPassword(), courier.getFirstName());
        Response response = doPostRequest("api/v1/courier/login", failLoginCourier);
        compareStatusCode(response, 404);
        compareResponseBodyMessage(response, "Учетная запись не найдена");
        deleteCourier();

    }

    @Test
    @DisplayName("Eсли какого-то поля нет, запрос возвращает ошибку")
    @Description("Для авторизации нужно передать все обязательные поля")
    public void cantAuthCourierWithoutloginTest() {
        createCourier();
        WithoutLoginCourier courierWithoutLogin = new WithoutLoginCourier(courier.getPassword());
        Response response = doPostRequest("api/v1/courier/login", courierWithoutLogin);
        compareStatusCode(response, 400);
        compareResponseBodyMessage(response, "Недостаточно данных для входа");
        deleteCourier();

    }
}
