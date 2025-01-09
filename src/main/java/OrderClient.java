import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderClient extends BaseHttpClient {

    private final static String PATH = "api/v1/orders";

    @Step("Send POST request to api/v1/orders")
    public Response createOrder(Object order) {
        return doPostRequest(PATH, order);
    }

    @Step("Get order track")
    public int getOrderTrack(Object order) {
        return doPostRequest(PATH, order)
                .then().extract().body().path("track");
    }

    @Step("Send PUT request to api/v1/orders/cancel")
    public void cancelOrder(int track) {
        doPutRequest(PATH + "/cancel?track=" + track);
    }

    @Step("Send GET request to api/v1/orders")
    public Response getOrderList() {
        return doGetRequest("api/v1/orders");
    }

    @Step("Compare status code")
    public void compareStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Response body contains track field")
    public void responseBodyContainsTrack(Response response) {
        response.then().assertThat().body("track", notNullValue());
    }

    @Step("Response body contains Orders")
    public void containsOrdersInBody(Response response) {
        response.then().assertThat().body("orders", notNullValue());
    }





}
