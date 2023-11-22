package lambich.dailyshop.service;

import lambich.dailyshop.beans.Order;
import lambich.dailyshop.beans.OrderItem;
import lambich.dailyshop.repository.OrderItemRepository;
import lambich.dailyshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public Order createOrder(Order order) {
        // Add any validation or business logic here
        return orderRepository.save(order);
    }

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        // Add any validation or business logic here
        return orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    public void confirmOrderReceipt(Long orderId) {
        // Retrieve the order by ID
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));

        // Update the order status to indicate receipt
        order.setOrderStatus("RECEIVED"); // Assuming you have an OrderStatus enum

        // Save the updated order
        orderRepository.save(order);
    }


    // Implement other methods as needed
}

