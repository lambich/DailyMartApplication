// ProductService.java
package lambich.dailyshop.service;

import lambich.dailyshop.beans.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Long productId);

    Product addProduct(Product product);

    Product updateProduct(Long productId, Product updatedProduct);

    void deleteProduct(Long productId);

    List<Product> getProductsByCategories(List<Long> selectedCategories);

    void saveProduct(Product newProduct);

    List<Product> getProductsByUserId(Long userId);

    List<Product> searchProducts(String keyword);
}
