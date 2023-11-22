package lambich.dailyshop.repository;

// CartRepository.java
import lambich.dailyshop.beans.Cart;
import lambich.dailyshop.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // Add custom queries if needed
    @Query(value = "SELECT * FROM cart WHERE user_id = :user_id", nativeQuery = true)
    Cart findCartByUserId(@Param("user_id") Long userId);

    @Query(value = "SELECT COUNT(*) FROM cart_item WHERE cart_id = :cart_id", nativeQuery = true)
    int countItemInCart(@Param("cart_id") Long cartId);

}

