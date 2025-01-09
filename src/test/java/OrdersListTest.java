import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class OrdersListTest {

    private OrderClient client = new OrderClient();

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void getOrderListTest() {
        Response response = client.getOrderList();
        client.compareStatusCode(response, SC_OK);
        client.containsOrdersInBody(response);
    }

}
