import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class CreateCourierTest {

    Courier courier = new Courier("test2025", "1234", "ivan");
    LoginCourier loginCourier = new LoginCourier(courier.getLogin(), courier.getPassword());
    CourierClient client = new CourierClient();

    @After
    public void cleanUp() {
        int id = client.getCourierId(loginCourier);
        client.deleteCourier(id);
    }

    @Test
    @DisplayName("Курьера можно создать")
    @Description("Запрос возвращает правильный код ответа")
    public void canCreateCourierTest() {
        Response response = client.createCourier(courier);
        client.compareStatusCode(response, SC_CREATED);
        client.compareResponseBodyOk(response);
    }

    @Test
    @DisplayName("Если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void cantCreateCourierSameLoginTest() {
        Courier courierSameLogin = new Courier(courier.getLogin(), "5678", "piter");
        client.createCourier(courier);
        Response response = client.createCourier(courierSameLogin);
        client.compareStatusCode(response, SC_CONFLICT);
        client.compareResponseBodyMessage(response, "Этот логин уже используется"); // баг
    }

    @Test
    @DisplayName("Невозможно создать курьера без обязательного поля login")
    public void cantCreateCourierWithoutloginTest() {
        client.createCourier(courier);
        Courier courierWithoutLogin = new Courier("", courier.getPassword(), courier.getFirstName());
        Response response = client.createCourier(courierWithoutLogin);
        client.compareStatusCode(response, SC_BAD_REQUEST);
        client.compareResponseBodyMessage(response, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Невозможно создать курьера без обязательного поля password")
    public void cantCreateCourierWithoutPasswordTest() {
        client.createCourier(courier);
        Courier courierWithoutPassword = new Courier(courier.getLogin(), "", courier.getFirstName());
        Response response = client.createCourier(courierWithoutPassword);
        client.compareStatusCode(response, SC_BAD_REQUEST);
        client.compareResponseBodyMessage(response, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Невозможно создать двух одинаковых курьеров")
    public void impossibleCreateTwoIdenticalCouriersTest() {
        Courier secondIdenticalCourier = new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName());
        client.createCourier(courier);
        Response response = client.createCourier(secondIdenticalCourier);
        client.compareStatusCode(response, SC_CONFLICT);
        client.compareResponseBodyMessage(response, "Этот логин уже используется");
    }
}
