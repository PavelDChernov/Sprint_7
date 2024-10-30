package service.utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import service.json.AuthData;
import service.json.Courier;

import static io.restassured.RestAssured.given;

final public class CourierApi {
    public static Response courierCreate(Courier courier) {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier")
                        .then()
                        .extract()
                        .response();
        return response;
    }

    public static Response courierCreate(String courier) {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier")
                        .then()
                        .extract()
                        .response();
        return response;
    }

    public static Response courierLogin(AuthData authData) {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body(authData)
                        .when()
                        .post("/api/v1/courier/login")
                        .then()
                        .extract()
                        .response();
        return response;
    }

    public static Response courierLogin(String authData) {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .and()
                        .body(authData)
                        .when()
                        .post("/api/v1/courier/login")
                        .then()
                        .extract()
                        .response();
        return response;
    }

    public static Response courierDelete(Integer id) {
        Response response =
                given()
                        .delete("/api/v1/courier/" + id.toString())
                        .then()
                        .extract()
                        .response();
        return response;
    }
}
