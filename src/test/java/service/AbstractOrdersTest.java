package service;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import service.json.Order;
import service.utils.OrderApi;

import java.util.List;

public class AbstractOrdersTest {
    protected List<Order> orderList = List.of(
                                              new Order(
                                                         "Лев",
                                                         "Толстой",
                                                          "Москва, Новотушинский пр-д, д. 8к1",
                                                      "Бульвар Адмирала Ушакова",
                                                           "8 922 222 22 22",
                                                         5,
                                                      "2024-12-12",
                                                        "Comment",
                                                                 List.of("BLACK","GREY")
                                              ),
                                              new Order(
                                                        "Федор",
                                                        "Достоевский",
                                                         "Москва, Профсоюзная ул., д. 125А",
                                                     "Тёплый Стан",
                                                          "8 800 333 33 33",
                                                        7,
                                                     "2024-12-31",
                                                       "Lorem ipsum dolor sit amet.",
                                                                List.of()
                                              )
    );

    @Before
    public void initTestData() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        for (Order order : orderList) {
            Response response = OrderApi.orderCreate(order);
            order.setTrack(response.path("track"));
        }
    }

    @After
    public void deleteTestData() {
        for (Order order : orderList) {
            OrderApi.orderCancel(order.getTrack());
        }
    }
}
