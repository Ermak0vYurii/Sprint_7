import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;


@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseHttpClient {

    private OrderClient client = new OrderClient();
    private Order order;

    private String color;

    public CreateOrderTest(String color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"BLACK"},
                {"GREY"},
                {"BLACK, GREY"},
                {""},
        };
    }

    @Test
    @DisplayName("Проверка оформления заказа")
    @Description("Оформление заказа с цветом BLACK, GREY, BLACK и GREY, без указания цвета")
    public void createOrderTest() {
        order = new Order("ivan",
                "petrov",
                "Lenina, 1",
                4,
                "89126644400",
                1,
                "2025-01-06",
                "comment",
                color.split(","));
        Response response = client.createOrder(order);
        client.compareStatusCode(response, SC_CREATED);
        client.responseBodyContainsTrack(response);
    }

    @After
    public void deleteOrder() {
        if (order != null) {
            int track = client.getOrderTrack(order);
            client.cancelOrder(track);
        }
    }
}
