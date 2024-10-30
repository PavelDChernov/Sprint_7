import jdk.jfr.Description;
import org.hamcrest.Matchers;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import service.AbstractOrdersTest;
import service.json.Orders;
import service.utils.OrderApi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class OrdersTests extends AbstractOrdersTest {
    @Test
    @DisplayName("Check that GET /api/v1/orders returns orders")
    @Description("GET /api/v1/orders returns orders, pageInfo and avaliableStations")
    public void getOrders() {
        Response response = OrderApi.orderGetOrders();
        assertEquals(200,response.statusCode());
        Orders orders = response.as(Orders.class);
        assertNotNull("Cannot Deserialize orders got null object", orders);
        assertFalse("Expected non empty \"orders\"[], got empty", orders.getOrders().isEmpty());
        assertTrue("Expected \"orders\" size 2+, got " + orders.getOrders().size(),orders.getOrders().size()>=2);
        assertThat("Expected non empty \"pageInfo\", got empty", orders.getPageInfo(), Matchers.notNullValue());
        assertEquals(0, orders.getPageInfo().getPage().intValue());
        assertTrue("Expected \"total\" size 2+, got " + orders.getPageInfo().getTotal(),orders.getPageInfo().getTotal()>=2);
        assertEquals(30, orders.getPageInfo().getLimit().intValue());
        assertFalse("Expected non empty \"availableStations\"[], got empty", orders.getAvailableStations().isEmpty());
        assertTrue("Expected \"availableStations\" size 2+, got " + orders.getAvailableStations().size(),orders.getAvailableStations().size()>=2);
    }
}
