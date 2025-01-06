import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

//курьера можно создать; +
//нельзя создать двух одинаковых курьеров;+
//чтобы создать курьера, нужно передать в ручку все обязательные поля;+
//запрос возвращает правильный код ответа;+
//успешный запрос возвращает ok: true; +
//если одного из полей нет, запрос возвращает ошибку;+
//если создать пользователя с логином, который уже есть, возвращается ошибка.+

public class CreateCourierTest extends BaseHttpClient {

    Courier courier = new Courier("test2025", "1234", "ivan");

@Test
    public void canCreateCourierTest() {
    Response response = doPostRequest("api/v1/courier", courier);
    response.then().statusCode(201);
    deleteCouriers();
}

@Test
    public void successfulRequestReturnOkTrueTest() {
    Response response = doPostRequest("api/v1/courier", courier);
    response.then().assertThat().body("ok", equalTo(true));
    deleteCouriers();
}

@Test
    public void cantCreateCourierSameLoginTest() {
    Courier courier2 = new Courier("test2025", "5678", "piter");
    doPostRequest("api/v1/courier", courier);
    Response response = doPostRequest("api/v1/courier", courier2);
    response.then().statusCode(409);
    deleteCouriers();
}

@Test
    public void cantCreateCourierWithoutloginTest() {
        Courier courierWithoutLogin = new Courier("", "1234", "ivan");
        Response response = doPostRequest("api/v1/courier", courierWithoutLogin);
        response.then().statusCode(400);
}

@Test
    public void cantCreateCourierWithoutPasswordTest() {
        Courier courierWithoutPassword = new Courier("test2025", "", "ivan");
        Response response = doPostRequest("api/v1/courier", courierWithoutPassword);
        response.then().statusCode(400);
}

    public void deleteCouriers(){
        LoginCourier loginCourier = new LoginCourier(courier.getLogin(), courier.getPassword());
        Response response = doPostRequest("api/v1/courier/login", loginCourier);
        int id = response.then().extract().body().path("id");
        doDeleteRequest("api/v1/courier/" + id);
    }
}
