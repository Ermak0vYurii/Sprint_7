import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrdersListTest extends BaseHttpClient {

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void getOrderListTest() {

        Response response = getOrderList();
        compareStatusCode(response, 200);
        containsOrdersInBody(response);
    }

    @Step("Send GET request to api/v1/orders")
    public Response getOrderList() {
        return doGetRequest("api/v1/orders");
    }
    @Step("Compare status code")
    public void compareStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Response body contains Orders")
    public void containsOrdersInBody(Response response) {
        response.then().assertThat().body("orders", notNullValue());
    }


}
