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
    protected final Courier courier = new Courier("accesso", "Passw0rt", "Gandalf");
    protected final AuthData authData = new AuthData("accesso", "Passw0rt");

    @Before
    public void initTestData() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        Response response = CourierApi.courierCreate(courier);
        assertTrue("Unable to create courier, got " + response.statusCode(), 201 == response.statusCode());
    }

    @After
    public void deleteTestData() {
        Response loginResponse = CourierApi.courierLogin(authData);
        if (200 == loginResponse.statusCode()) {
            CourierApi.courierDelete(loginResponse.path("id"));
        }
    }
}
