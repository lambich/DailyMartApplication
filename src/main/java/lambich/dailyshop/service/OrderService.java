package lambich.dailyshop.service;

import lambich.dailyshop.beans.Order;
import lambich.dailyshop.beans.OrderItem;

import javax.transaction.Transactional;
import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

    List<Order> getAllOrdersByUser(Long userId);

    Order getOrderById(Long orderId);

    Order createOrder(Order order);

    OrderItem createOrderItem(OrderItem orderItem);

    @Transactional
    void confirmOrderReceipt(Long orderId);

    // Add more methods as needed
}
