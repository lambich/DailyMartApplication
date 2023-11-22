package lambich.dailyshop.repository;

import lambich.dailyshop.beans.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // You can add custom query methods if needed
    List<Order> findByUserId(Long userId);
}
