package service;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import service.json.AuthData;
import service.json.Courier;
import service.utils.CourierApi;

public abstract class AbstractCourierCreationTest {
    protected final Courier COURIER = new Courier("accesso", "Passw0rt", "Gandalf");
    protected final AuthData AUTH_DATA = new AuthData("accesso", "Passw0rt");
    protected final String LOGIN = "\"login\":\"accesso\"";
    protected final String PASSWORD = "\"password\":\"Passw0rt\"";
    protected final String FIRST_NAME = "\"firstName\":\"Gandalf\"";

    @Before
    public void initTestData() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @After
    public void deleteTestData() {
        Response loginResponse = CourierApi.courierLogin(new AuthData(COURIER.getLogin(), COURIER.getPassword()));
        if (200 == loginResponse.statusCode()) {
            CourierApi.courierDelete(loginResponse.path("id"));
        }
    }
}
