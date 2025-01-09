import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class LoginCourierTest {

    Courier courier = new Courier("test2025", "1234", "ivan");
    LoginCourier loginCourier = new LoginCourier(courier.getLogin(), courier.getPassword());
    CourierClient client = new CourierClient();

    @Before
    public void setUp() {
        client.createCourier(courier);
    }

    @After
    public void cleanUp() {
            int CourierId = client.getCourierId(loginCourier);
            client.deleteCourier(CourierId);
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void successfulAuthCourierTest() {
        Response response = client.loginCourier(loginCourier);
        client.compareStatusCode(response, SC_OK);
        client.responseBodyLoginHaveId(response);
    }

    @Test
    @DisplayName("Авторизация несуществующего курьера возвращает ошибку")
    public void authNonExistentCourierTest() {
        LoginCourier loginNonExistentCourier = new LoginCourier("lkjhtyubx", "0000");
        Response response = client.loginCourier(loginNonExistentCourier);
        client.compareStatusCode(response, SC_NOT_FOUND);
        client.compareResponseBodyMessage(response, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Авторизация с неправильно указанным логином возвращает ошибку")
    public void cantAuthCourierFailLoginTest() {
        Courier failLoginCourier = new Courier("test2052", courier.getPassword(), courier.getFirstName());
        Response response = client.loginCourier(failLoginCourier);
        client.compareStatusCode(response, SC_NOT_FOUND);
        client.compareResponseBodyMessage(response, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Eсли какого-то поля нет, запрос возвращает ошибку")
    @Description("Для авторизации нужно передать все обязательные поля")
    public void cantAuthCourierWithoutloginTest() {
        WithoutLoginCourier courierWithoutLogin = new WithoutLoginCourier(courier.getPassword());
        Response response = client.loginCourier(courierWithoutLogin);
        client.compareStatusCode(response, SC_BAD_REQUEST);
        client.compareResponseBodyMessage(response, "Недостаточно данных для входа");
    }
}
