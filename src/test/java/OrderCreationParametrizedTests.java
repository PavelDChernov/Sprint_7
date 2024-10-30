import jdk.jfr.Description;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import service.AbstractOrderCreationTest;
import service.utils.OrderApi;
import service.json.Order;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderCreationParametrizedTests extends AbstractOrderCreationTest {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;

    public OrderCreationParametrizedTests(String firstName, String lastName, String address, String metroStation, String phone, Integer rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {
                        "Николай",
                        "Гоголь",
                        "Москва, Окружной пр-д, д. 3",
                        "Черкизовская",
                        "+7 900 000 00 00",
                        1,
                        "2024-11-30",
                        "Comment",
                        List.of("BLACK")
                },
                {
                        "Александр",
                        "Пушкин",
                        "Москва, Гусятников пер., д. 3/1с1",
                        "Чистые Пруды",
                        "+7 911 111 11 11",
                        3,
                        "2024-12-01",
                        "Комментарий",
                        List.of("GREY")
                },
                {
                        "Лев",
                        "Толстой",
                        "Москва, Новотушинский пр-д, д. 8к1",
                        "Бульвар Адмирала Ушакова",
                        "8 922 222 22 22",
                        5,
                        "2024-12-12",
                        ":3",
                        List.of("BLACK","GREY")
                },
                {
                        "Федор",
                        "Достоевский",
                        "Москва, Профсоюзная ул., д. 125А",
                        "Тёплый Стан",
                        "8 800 333 33 33",
                        7,
                        "2024-12-31",
                        "Lorem ipsum dolor sit amet.",
                        List.of()
                }
        };

    }

    @Test
    @DisplayName("Check status code of POST /api/v1/orders on success")
    @Description("Status 201 on successful order creation")
    public void responseCode200OrderCreation() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        response = OrderApi.orderCreate(order);
        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("Check response body of POST /api/v1/orders on success")
    @Description("Response \"track: {{number}}\" on successful courier creation")
    public void responseBodyContainsTrackIdOrderCreation() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        response = OrderApi.orderCreate(order);
        assertTrue("No \"track\" key in body", response.body().asString().contains("track"));
        assertTrue("Expected instance of Integer, got " + response.path("track").getClass().getName(),
                    Integer.class.isInstance(response.path("track")));
    }
}
