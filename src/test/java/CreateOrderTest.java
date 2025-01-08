import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseHttpClient {

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
        Order order = new Order("ivan",
                                "petrov",
                                "Lenina, 1",
                                4,
                                "89126644400",
                                1,
                                "2025-01-06",
                                "comment",
                                color.split(","));
        Response response = createOrder(order);
        compareStatusCode(response, 201);
                responseBodyContainsTrack(response);
    }

    @Step("Send POST request to api/v1/orders")
    public Response createOrder(Object order) {
        return doPostRequest("api/v1/orders", order);
    }

    @Step("Compare status code")
    public void compareStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Response body contains track field")
    public void responseBodyContainsTrack(Response response) {
        response.then().assertThat().body("track", notNullValue());
    }
}
