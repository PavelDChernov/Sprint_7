package service;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import service.json.AuthData;
import service.json.Courier;
import service.utils.CourierApi;

import static org.junit.Assert.assertTrue;

public abstract class AbstractCourierLoginTest {
    protected final Courier COURIER = new Courier("accesso", "Passw0rt", "Gandalf");
    protected final AuthData AUTH_DATA = new AuthData("accesso", "Passw0rt");
    protected final String LOGIN = "\"login\":\"accesso\"";
    protected final String PASSWORD = "\"password\":\"Passw0rt\"";

    @Before
    public void initTestData() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        Response response = CourierApi.courierCreate(COURIER);
        assertTrue("Unable to create courier, got " + response.statusCode(), 201 == response.statusCode());
    }

    @After
    public void deleteTestData() {
        Response loginResponse = CourierApi.courierLogin(AUTH_DATA);
        if (200 == loginResponse.statusCode()) {
            CourierApi.courierDelete(loginResponse.path("id"));
        }
    }
}
