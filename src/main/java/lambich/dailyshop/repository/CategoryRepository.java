package lambich.dailyshop.repository;

// CategoryRepository.java
import lambich.dailyshop.beans.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // You can add custom queries here if needed
}

