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

    @Test
    public void successfulAuthCourierTest() {
        createCourier();
        loginCourier().
                then().statusCode(200);
        deleteCourier();
    }

    @Test
    public void successfulRequestReturnIdTest() {
        createCourier();
        loginCourier()
                .then().assertThat().body("id", notNullValue());
        deleteCourier();
    }

    @Test
    public void authNonExistentCourierTest() {
        loginCourier()
                .then().statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void cantAuthCourierWithoutloginTest() {
        createCourier();
        FailLoginCourier courierWithoutLogin = new FailLoginCourier(courier.getPassword());
        Response response = doPostRequest("api/v1/courier/login", courierWithoutLogin);
        response.then().statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
        deleteCourier();

    }


}
