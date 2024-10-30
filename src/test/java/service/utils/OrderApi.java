package service.utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import service.json.Order;

import static io.restassured.RestAssured.given;

final public class OrderApi {
    public static Response orderCreate(Order order) {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders")
                        .then()
                        .extract()
                        .response();
        return response;
    }

    public static Response orderCreate(String requestBody) {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body(requestBody)
                        .when()
                        .post("/api/v1/orders")
                        .then()
                        .extract()
                        .response();
        return response;
    }

    public static Response orderCancel(Integer track) {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body("{\"track\":" + track.toString() + "}")
                        .when()
                        .put("/api/v1/orders/cancel")
                        .then()
                        .extract()
                        .response();
        return response;
    }

    public static Response orderGetOrders() {
        Response response =
                given()
                        .when()
                        .get("/api/v1/orders")
                        .then()
                        .extract()
                        .response();
        return response;
    }
}
