package service;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import service.utils.OrderApi;

import static org.junit.Assert.assertTrue;

public abstract class AbstractOrderCreationTest {
    protected Response response = null;

    @Before
    public void initTestData() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        response = null;
    }

    @After
    public void deleteTestData() {
        assertTrue("No \"track\" key in body", response.body().asString().contains("track"));
        OrderApi.orderCancel(response.path("track"));
        response = null;
    }
}
