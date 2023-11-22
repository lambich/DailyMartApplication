package lambich.dailyshop.repository;

import lambich.dailyshop.beans.CartItem;
import lambich.dailyshop.beans.Product;
import lambich.dailyshop.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByProductAndCartUser(Product product, User cart_user);

    void deleteByProductAndCartUser(Product product, User cartUser);

    List<CartItem> findByCartId(Long cartId);
}

