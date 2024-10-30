import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import service.AbstractCourierLoginTest;
import service.utils.CourierApi;
import service.json.AuthData;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class CourierLoginTests extends AbstractCourierLoginTest {
    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login on success")
    @Description("Status 201 on successful login")
    public void responseCode200CorrectLoginPassword() {
        Response response = CourierApi.courierLogin(AUTH_DATA);
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login on success")
    @Description("Response \"id: {{number}}\" on successful login")
    public void responseBodyContainsIdIntCorrectLoginPassword() {
        Response response = CourierApi.courierLogin(AUTH_DATA);
        assertTrue("No \"id\" key in body", response.body().asString().contains("id"));
        assertTrue("Expected instance of Integer, got " + response.path("id").getClass().getName(),
                    Integer.class.isInstance(response.path("id")));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login on incorrect login")
    @Description("Status 404 on incorrect login")
    public void responseCode404WhenIncorrectLogin() {
        AuthData incorrectLogin = new AuthData("aacceessoo", AUTH_DATA.getPassword());
        Response response = CourierApi.courierLogin(incorrectLogin);
        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login on incorrect login")
    @Description("Response \"message: Учетная запись не найдена\" on incorrect login")
    public void responseMessageCheckWhenIncorrectLogin() {
        AuthData incorrectLogin = new AuthData("aacceessoo", AUTH_DATA.getPassword());
        Response response = CourierApi.courierLogin(incorrectLogin);
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertThat(response.path("message"), startsWith("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login on incorrect password")
    @Description("Status 404 on incorrect password")
    public void responseCode404WhenIncorrectPassword() {
        AuthData incorrectPassword = new AuthData(AUTH_DATA.getLogin(), "1111");
        Response response = CourierApi.courierLogin(incorrectPassword);
        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login on incorrect password")
    @Description("Response \"message: Учетная запись не найдена\" on incorrect password")
    public void responseMessageCheckWhenIncorrectPassword() {
        AuthData incorrectPassword = new AuthData(AUTH_DATA.getLogin(), "1111");
        Response response = CourierApi.courierLogin(incorrectPassword);
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertThat(response.path("message"), startsWith("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login if provided login and password not exist")
    @Description("Status 404 if provided login and password not exist")
    public void responseCode404WhenNonExistentCourier() {
        Response loginResponse = CourierApi.courierLogin(AUTH_DATA);
        if (200 == loginResponse.statusCode()) {
            CourierApi.courierDelete(loginResponse.path("id"));
        }
        Response response = CourierApi.courierLogin(AUTH_DATA);
        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login if provided login and password not exist")
    @Description("Response \"message: Учетная запись не найдена\" if provided login and password not exist")
    public void responseMessageCheckWhenNonExistentCourier() {
        Response loginResponse = CourierApi.courierLogin(AUTH_DATA);
        if (200 == loginResponse.statusCode()) {
            CourierApi.courierDelete(loginResponse.path("id"));
        }
        Response response = CourierApi.courierLogin(AUTH_DATA);
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertThat(response.path("message"), startsWith("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login without login")
    @Description("Status 400 without login")
    public void responseCode400WithoutLogin() {
        Response response = CourierApi.courierLogin("{" + PASSWORD + "}");
        assertEquals(400, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login without login")
    @Description("Response \"message: Недостаточно данных для входа\" without login")
    public void responseMessageCheckWithoutLogin() {
        Response response = CourierApi.courierLogin("{" + PASSWORD + "}");
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для входа\", got message: " + response.path("message"),
                    response.path("message").equals("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login on null login")
    @Description("Status 400 on null login")
    public void responseCode400NullLogin() {
        Response response = CourierApi.courierLogin("{\"login\":null," + PASSWORD + "}");
        assertEquals(400, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login on null login")
    @Description("Response \"message: Недостаточно данных для входа\" on null login")
    public void responseMessageCheckNullLogin() {
        Response response = CourierApi.courierLogin("{\"login\":null," + PASSWORD + "}");
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для входа\", got message: " + response.path("message"),
                response.path("message").equals("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login without password")
    @Description("Status 400 without password")
    public void responseCode400WithoutPassword() {
        Response response = CourierApi.courierLogin("{" + LOGIN + "}");
        assertEquals(400, response.statusCode());
        // Фактический результат: 504, таймаут
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login without password")
    @Description("Response \"message: Недостаточно данных для входа\" without password")
    public void responseMessageCheckWithoutPassword() {
        Response response = CourierApi.courierLogin("{" + LOGIN + "}");
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для входа\", got message: " + response.path("message"),
                    response.path("message").equals("Недостаточно данных для входа"));
        // Фактический результат: 504, таймаут
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login on null password")
    @Description("Status 400 on null password")
    public void responseCode400NullPassword() {
        Response response = CourierApi.courierLogin("{" + LOGIN + ",\"password\":null}");
        assertEquals(400, response.statusCode());
        // Фактический результат: 504, таймаут
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login on null password")
    @Description("Response \"message: Недостаточно данных для входа\" on null password")
    public void responseMessageCheckNullPassword() {
        Response response = CourierApi.courierLogin("{" + LOGIN + ",\"password\":null}");
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для входа\", got message: " + response.path("message"),
                response.path("message").equals("Недостаточно данных для входа"));
        // Фактический результат: 504, таймаут
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login on empty body")
    @Description("Status 400 on empty body")
    public void responseCode400EmptyBody() {
        Response response = CourierApi.courierLogin("{}");
        assertEquals(400, response.statusCode());
        // Фактический результат: 504, таймаут
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login on empty body")
    @Description("Response \"message: Недостаточно данных для входа\" on empty body")
    public void responseMessageCheckEmptyBody() {
        Response response = CourierApi.courierLogin("{}");
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для входа\", got message: " + response.path("message"),
                response.path("message").equals("Недостаточно данных для входа"));
        // Фактический результат: 504, таймаут
    }
}
