package service;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import service.json.AuthData;
import service.json.Courier;
import service.utils.CourierApi;

public abstract class AbstractCourierCreationTest {
    protected final Courier courier = new Courier("accesso", "Passw0rt", "Gandalf");

    @Before
    public void initTestData() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @After
    public void deleteTestData() {
        Response loginResponse = CourierApi.courierLogin(new AuthData(courier.getLogin(), courier.getPassword()));
        if (200 == loginResponse.statusCode()) {
            CourierApi.courierDelete(loginResponse.path("id"));
        }
    }
}
