import jdk.jfr.Description;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import service.AbstractOrderCreationTest;
import service.utils.OrderApi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderCreationNonParametrizedTests extends AbstractOrderCreationTest {
    private final String ORDER_NO_COLOR =    "{\"firstName\":\"Иван\"," +
                                       "\"lastName\":\"Крылов\"," +
                                       "\"address\":\"Москва, ул. Маросейка, д. 7/8\"," +
                                       "\"metroStation\":\"Китай-Город\"," +
                                       "\"phone\":\"+7 912 345 67 89\"," +
                                       "\"rentTime\":2," +
                                       "\"deliveryDate\":\"2024-11-14\"," +
                                       "\"comment\":\"А зачем?\"}";

    private final String ORDER_NULL_COLOR =  "{\"firstName\":\"Михаил\"," +
                                       "\"lastName\":\"Лермонтов\"," +
                                       "\"address\":\"Москва, Дмитровское ш., д. 46к1\"," +
                                       "\"metroStation\":\"Владыкино\"," +
                                       "\"phone\":\"+7 987 654 32 10\"," +
                                       "\"rentTime\":1," +
                                       "\"deliveryDate\":\"2024-11-21\"," +
                                       "\"comment\":\"\"," +
                                       "\"color\":null}";

    @Test
    @DisplayName("Check status code of POST /api/v1/orders without color")
    @Description("Status 201 on order creation without color")
    public void responseCode200OrderCreationWithoutColor() {
        response = OrderApi.orderCreate(ORDER_NO_COLOR);
        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/orders without color")
    @Description("Response \"track: {{number}}\" on order creation without color")
    public void responseBodyContainsTrackIdOrderWithoutColor() {
        response = OrderApi.orderCreate(ORDER_NO_COLOR);
        assertTrue("No \"track\" key in body", response.body().asString().contains("track"));
        assertTrue("Expected instance of Integer, got " + response.path("track").getClass().getName(),
                    Integer.class.isInstance(response.path("track")));
    }

    @Test
    @DisplayName("Check status code of POST /api/v1/orders with null color")
    @Description("Status 201 on order creation with null color")
    public void responseCode200OrderCreationNullColor() {
        response = OrderApi.orderCreate(ORDER_NULL_COLOR);
        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/orders with null color")
    @Description("Response \"track: {{number}}\" on order creation with null color")
    public void responseBodyContainsTrackIdOrderCreationNullColor() {
        response = OrderApi.orderCreate(ORDER_NULL_COLOR);
        assertTrue("No \"track\" key in body", response.body().asString().contains("track"));
        assertTrue("Expected instance of Integer, got " + response.path("track").getClass().getName(),
                Integer.class.isInstance(response.path("track")));
    }
}
