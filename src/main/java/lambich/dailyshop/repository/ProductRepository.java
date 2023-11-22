// ProductRepository.java
package lambich.dailyshop.repository;

import lambich.dailyshop.beans.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Add custom queries if needed

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.id IN :categoryIds")
    List<Product> findByCategories(@Param("categoryIds") List<Long> categoryIds);

    List<Product> findByUserId(Long userId);
    List<Product> findByNameContainingIgnoreCase(String name);
}
