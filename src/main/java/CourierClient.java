import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierClient extends BaseHttpClient {

    private final static String PATH = "api/v1/courier";

    @Step("Send POST request to api/v1/courier")
    public Response createCourier(Object courier) {
        return doPostRequest(PATH, courier);
    }

    @Step("Send POST request to api/v1/courier/login")
    public Response loginCourier(Object loginCourier) {
        return doPostRequest(PATH + "/login", loginCourier);
    }

    @Step("Get courier id")
    public int getCourierId(Object loginCourier) {
       return doPostRequest(PATH + "/login", loginCourier)
               .then().extract().body().path("id");
    }

    @Step("Send DELETE request to api/v1/courier/:id")
    public void deleteCourier(int CourierId) {
        doDeleteRequest(PATH + "/" +  CourierId);
    }

    @Step("Compare status code")
    public void compareStatusCode(Response response, int code){
        response.then().statusCode(code);
    }

    @Step("Compare response body")
    public void compareResponseBodyOk(Response response){
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Step("Successful request return courier id")
    public void responseBodyLoginHaveId(Response response){
        response.then().assertThat().body("id", notNullValue());
    }

    @Step("Compare body error message")
    public void compareResponseBodyMessage(Response response, String message) {
        response.then().assertThat().body("message", equalTo(message));
    }
}
