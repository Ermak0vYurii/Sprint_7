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
        Response response = doPostRequest("api/v1/orders", order);
        response.then().statusCode(201)
                .assertThat().body("track", notNullValue());
    }
}
