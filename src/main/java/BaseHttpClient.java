import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;


public abstract class BaseHttpClient {

    public static final String URL = "https://qa-scooter.praktikum-services.ru/";

    private RequestSpecification baseRequestSpec = new RequestSpecBuilder()
            .setBaseUri(URL)
            .addHeader("Content-type", "application/json")
            .setRelaxedHTTPSValidation()
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .addFilter(new ErrorLoggingFilter())
            .build();

    protected Response doPostRequest(String path, Object body) {
        return given()
                .spec(baseRequestSpec)
                .body(body)
                .post(path)
                .thenReturn();
    }

    protected Response doDeleteRequest(String path) {
        return  given()
                .spec(baseRequestSpec)
                .delete(path)
                .thenReturn();
    }

    protected Response doGetRequest(String path) {
        return given()
                .spec(baseRequestSpec)
                .get(path)
                .thenReturn();
    }

    protected Response doPutRequest(String path) {
        return given()
                .spec(baseRequestSpec)
                .put(path)
                .thenReturn();
    }

}
