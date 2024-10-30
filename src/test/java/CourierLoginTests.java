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
        Response response = CourierApi.courierLogin(authData);
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login on success")
    @Description("Response \"id: {{number}}\" on successful login")
    public void responseBodyContainsIdIntCorrectLoginPassword() {
        Response response = CourierApi.courierLogin(authData);
        assertTrue("No \"id\" key in body", response.body().asString().contains("id"));
        assertTrue("Expected instance of Integer, got " + response.path("id").getClass().getName(),
                    Integer.class.isInstance(response.path("id")));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login on incorrect login")
    @Description("Status 404 on incorrect login")
    public void responseCode404WhenIncorrectLogin() {
        AuthData incorrectLogin = new AuthData("aacceessoo", "Passw0rt");
        Response response = CourierApi.courierLogin(incorrectLogin);
        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login on incorrect login")
    @Description("Response \"message: Учетная запись не найдена\" on incorrect login")
    public void responseMessageCheckWhenIncorrectLogin() {
        AuthData incorrectLogin = new AuthData("aacceessoo", "Passw0rt");
        Response response = CourierApi.courierLogin(incorrectLogin);
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertThat(response.path("message"), startsWith("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login on incorrect password")
    @Description("Status 404 on incorrect password")
    public void responseCode404WhenIncorrectPassword() {
        AuthData incorrectPassword = new AuthData("accesso", "1111");
        Response response = CourierApi.courierLogin(incorrectPassword);
        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login on incorrect password")
    @Description("Response \"message: Учетная запись не найдена\" on incorrect password")
    public void responseMessageCheckWhenIncorrectPassword() {
        AuthData incorrectPassword = new AuthData("accesso", "1111");
        Response response = CourierApi.courierLogin(incorrectPassword);
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertThat(response.path("message"), startsWith("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login if provided login and password not exist")
    @Description("Status 404 if provided login and password not exist")
    public void responseCode404WhenNonExistentCourier() {
        AuthData incorrectPassword = new AuthData("somerandomusername100500", "100500100500");
        Response loginResponse = CourierApi.courierLogin(authData);
        if (200 == loginResponse.statusCode()) {
            CourierApi.courierDelete(loginResponse.path("id"));
        }
        Response response = CourierApi.courierLogin(incorrectPassword);
        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login if provided login and password not exist")
    @Description("Response \"message: Учетная запись не найдена\" if provided login and password not exist")
    public void responseMessageCheckWhenNonExistentCourier() {
        AuthData incorrectPassword = new AuthData("somerandomusername100500", "100500100500");
        Response loginResponse = CourierApi.courierLogin(authData);
        if (200 == loginResponse.statusCode()) {
            CourierApi.courierDelete(loginResponse.path("id"));
        }
        Response response = CourierApi.courierLogin(incorrectPassword);
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertThat(response.path("message"), startsWith("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login without login")
    @Description("Status 400 without login")
    public void responseCode400WithoutLogin() {
        String requestBody = new String("{\"password\":\"Passw0rt\"}");
        Response response = CourierApi.courierLogin(requestBody);
        assertEquals(400, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login without login")
    @Description("Response \"message: Недостаточно данных для входа\" without login")
    public void responseMessageCheckWithoutLogin() {
        String requestBody = new String("{\"password\":\"Passw0rt\"}");
        Response response = CourierApi.courierLogin(requestBody);
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для входа\", got message: " + response.path("message"),
                    response.path("message").equals("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login on null login")
    @Description("Status 400 on null login")
    public void responseCode400NullLogin() {
        String requestBody = new String("{\"login\":null,\"password\":\"Passw0rt\"}");
        Response response = CourierApi.courierLogin(requestBody);
        assertEquals(400, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login on null login")
    @Description("Response \"message: Недостаточно данных для входа\" on null login")
    public void responseMessageCheckNullLogin() {
        String requestBody = new String("{\"login\":null,\"password\":\"Passw0rt\"}");
        Response response = CourierApi.courierLogin(requestBody);
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для входа\", got message: " + response.path("message"),
                response.path("message").equals("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login without password")
    @Description("Status 400 without password")
    public void responseCode400WithoutPassword() {
        String requestBody = new String("{\"login\":\"accesso\"}");
        Response response = CourierApi.courierLogin(requestBody);
        assertEquals(400, response.statusCode());
        // Фактический результат: 504, таймаут
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login without password")
    @Description("Response \"message: Недостаточно данных для входа\" without password")
    public void responseMessageCheckWithoutPassword() {
        String requestBody = new String("{\"login\":\"accesso\"}");
        Response response = CourierApi.courierLogin(requestBody);
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для входа\", got message: " + response.path("message"),
                    response.path("message").equals("Недостаточно данных для входа"));
        // Фактический результат: 504, таймаут
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login on null password")
    @Description("Status 400 on null password")
    public void responseCode400NullPassword() {
        String requestBody = new String("{\"login\":\"accesso\",\"password\":null}");
        Response response = CourierApi.courierLogin(requestBody);
        assertEquals(400, response.statusCode());
        // Фактический результат: 504, таймаут
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login on null password")
    @Description("Response \"message: Недостаточно данных для входа\" on null password")
    public void responseMessageCheckNullPassword() {
        String requestBody = new String("{\"login\":\"accesso\",\"password\":null}");
        Response response = CourierApi.courierLogin(requestBody);
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для входа\", got message: " + response.path("message"),
                response.path("message").equals("Недостаточно данных для входа"));
        // Фактический результат: 504, таймаут
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier/login on empty body")
    @Description("Status 400 on empty body")
    public void responseCode400EmptyBody() {
        String requestBody = new String("{}");
        Response response = CourierApi.courierLogin(requestBody);
        assertEquals(400, response.statusCode());
        // Фактический результат: 504, таймаут
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier/login on empty body")
    @Description("Response \"message: Недостаточно данных для входа\" on empty body")
    public void responseMessageCheckEmptyBody() {
        String requestBody = new String("{}");
        Response response = CourierApi.courierLogin(requestBody);
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для входа\", got message: " + response.path("message"),
                response.path("message").equals("Недостаточно данных для входа"));
        // Фактический результат: 504, таймаут
    }
}
