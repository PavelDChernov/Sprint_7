import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import service.AbstractCourierCreationTest;
import service.utils.CourierApi;
import service.json.AuthData;
import service.json.Courier;

import static org.junit.Assert.*;

public class CourierCreationTests extends AbstractCourierCreationTest {
    @Test
    @DisplayName("Check status code of POST /api/v1/courier on success")
    @Description("Status 201 on successful courier creation")
    public void responseCode201CourierCreation() {
        Response response = CourierApi.courierCreate(COURIER);
        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/courier on success")
    @Description("Response \"ok: true\" on successful courier creation")
    public void responseBodyContainsOkTrueCourierCreation() {
        Response response = CourierApi.courierCreate(COURIER);
        assertTrue("No \"ok\" key in body", response.body().asString().contains("ok"));
        assertTrue("Expected \"ok: true\", got ok: " + response.path("ok"),response.path("ok"));
    }

    @Test
    @DisplayName("Check courier creation with POST /api/v1/courier without firstName")
    @Description("Courier can be created without firstName")
    public void courierCreatesWithoutFirstName() {
        Response response = CourierApi.courierCreate(AUTH_DATA);
        assertEquals(201, response.statusCode());
        Response loginResponse = CourierApi.courierLogin(AUTH_DATA);
        if (200 == loginResponse.statusCode()) {
            CourierApi.courierDelete(loginResponse.path("id"));
        }
        // По апидоке обязательны только логин и пароль
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier if courier already exists")
    @Description("Status 409 if courier already exists")
    public void responseCode409WhenCourierAlreadyExists() {
        Response response = CourierApi.courierCreate(COURIER);
        assertEquals(201, response.statusCode());
        response = CourierApi.courierCreate(COURIER);
        if (response.statusCode() < 400) {
            Response loginResponse = CourierApi.courierLogin(new AuthData(COURIER.getLogin(), COURIER.getPassword()));
            if (200 == loginResponse.statusCode()) {
                CourierApi.courierDelete(loginResponse.path("id"));
            }
        }
        assertEquals(409, response.statusCode());
    }

    @Test
    @DisplayName("Check error message of POST /api/v1/courier if courier already exists")
    @Description("Response \"message: Этот логин уже используется\" if courier already exists")
    public void responseMessageCheckWhenCourierAlreadyExists() {
        Response response = CourierApi.courierCreate(COURIER);
        assertEquals(201, response.statusCode());
        response = CourierApi.courierCreate(COURIER);
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Этот логин уже используется\", got message: " + response.path("message"),
                    response.path("message").equals("Этот логин уже используется"));
        // Тест падает. По апидоке "message": "Этот логин уже используется" фактически "message": "Этот логин уже используется. Попробуйте другой."
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier if login already exists")
    @Description("Status 409 if login already exists")
    public void responseCode409WhenCourierLoginAlreadyExists() {
        Response response = CourierApi.courierCreate(COURIER);
        assertEquals(201, response.statusCode());
        Courier sameLoginCourier = new Courier("accesso", "2Passw0rt2", "GandalfTheWhite");
        response = CourierApi.courierCreate(sameLoginCourier);
        if (response.statusCode() < 400) {
            Response loginResponse = CourierApi.courierLogin(new AuthData(COURIER.getLogin(), COURIER.getPassword()));
            if (200 == loginResponse.statusCode()) {
                CourierApi.courierDelete(loginResponse.path("id"));
            }
        }
        assertEquals(409, response.statusCode());
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier without password")
    @Description("Status 400 if invoked without password")
    public void responseCode400WithoutPassword() {
        Response response = CourierApi.courierCreate("{" + LOGIN + "," + FIRST_NAME + "}");
        assertEquals(400, response.statusCode());
    }

    @Test
    @DisplayName("Check error message of POST /api/v1/courier without password")
    @Description("Response \"message: Недостаточно данных для создания учетной записи\" if invoked without password")
    public void responseMessageCheckWithoutPassword() {
        Response response = CourierApi.courierCreate("{" + LOGIN + "," + FIRST_NAME + "}");
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для создания учетной записи\", got message: " + response.path("message"),
                    response.path("message").equals("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier with null password")
    @Description("Status 400 if invoked with null password")
    public void responseCode400NullPassword() {
        Response response = CourierApi.courierCreate("{" + LOGIN + ",\"password\":null," + FIRST_NAME + "}");
        assertEquals(400, response.statusCode());
    }

    @Test
    @DisplayName("Check error message of POST /api/v1/courier with null password")
    @Description("Response \"message: Недостаточно данных для создания учетной записи\" if invoked with null password")
    public void responseMessageCheckNullPassword() {
        Response response = CourierApi.courierCreate("{" + LOGIN + ",\"password\":null," + FIRST_NAME + "}");
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для создания учетной записи\", got message: " + response.path("message"),
                response.path("message").equals("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier without login")
    @Description("Status 400 if invoked without login")
    public void responseCode400WithoutLogin() {
        Response response = CourierApi.courierCreate("{" + PASSWORD + "," + FIRST_NAME + "}");
        assertEquals(400, response.statusCode());
    }

    @Test
    @DisplayName("Check error message of POST /api/v1/courier without login")
    @Description("Response \"message: Недостаточно данных для создания учетной записи\" if invoked without login")
    public void responseMessageCheckWithoutLogin() {
        Response response = CourierApi.courierCreate("{" + PASSWORD + "," + FIRST_NAME + "}");
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для создания учетной записи\", got message: " + response.path("message"),
                    response.path("message").equals("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier with null login")
    @Description("Status 400 if invoked with null login")
    public void responseCode400NullLogin() {
        Response response = CourierApi.courierCreate("{\"login\":null," + PASSWORD + "," + FIRST_NAME + "}");
        assertEquals(400, response.statusCode());
    }

    @Test
    @DisplayName("Check error message of POST /api/v1/courier with null login")
    @Description("Response \"message: Недостаточно данных для создания учетной записи\" if invoked with null login")
    public void responseMessageCheckNullLogin() {
        Response response = CourierApi.courierCreate("{\"login\":null," + PASSWORD + "," + FIRST_NAME + "}");
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для создания учетной записи\", got message: " + response.path("message"),
                response.path("message").equals("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier without login and password")
    @Description("Status 400 if invoked without login and password")
    public void responseCode400WithoutLoginAndPasswordInBody() {
        Response response = CourierApi.courierCreate("{" + FIRST_NAME + "}");
        assertEquals(400, response.statusCode());
    }

    @Test
    @DisplayName("Check error message of POST /api/v1/courier without login and password")
    @Description("Response \"message: Недостаточно данных для создания учетной записи\" if invoked without login and password")
    public void responseMessageCheckWithoutLoginAndPasswordInBody() {
        Response response = CourierApi.courierCreate("{" + FIRST_NAME + "}");
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для создания учетной записи\", got message: " + response.path("message"),
                    response.path("message").equals("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier with null login and password")
    @Description("Status 400 if invoked with null login and password")
    public void responseCode400NullLoginAndPasswordInBody() {
        Response response = CourierApi.courierCreate("{\"login\":null,\"password\":null," + FIRST_NAME + "}");
        assertEquals(400, response.statusCode());
    }

    @Test
    @DisplayName("Check error message of POST /api/v1/courier with null login and password")
    @Description("Response \"message: Недостаточно данных для создания учетной записи\" if invoked with null login and password")
    public void responseMessageCheckNullLoginAndPasswordInBody() {
        Response response = CourierApi.courierCreate("{\"login\":null,\"password\":null," + FIRST_NAME + "}");
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для создания учетной записи\", got message: " + response.path("message"),
                response.path("message").equals("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/courier with empty body")
    @Description("Status 400 if invoked with empty body")
    public void responseCode400EmptyBody() {
        Response response = CourierApi.courierCreate("{}");
        assertEquals(400, response.statusCode());
    }

    @Test
    @DisplayName("Check error message of POST /api/v1/courier with empty body")
    @Description("Response \"message: Недостаточно данных для создания учетной записи\" if invoked with empty body")
    public void responseMessageCheckEmptyBody() {
        Response response = CourierApi.courierCreate("{}");
        assertTrue("No \"message\" key in body", response.body().asString().contains("message"));
        assertTrue("Expected \"message: Недостаточно данных для создания учетной записи\", got message: " + response.path("message"),
                response.path("message").equals("Недостаточно данных для создания учетной записи"));
    }
}