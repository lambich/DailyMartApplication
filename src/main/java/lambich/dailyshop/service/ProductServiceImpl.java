// ProductServiceImpl.java
package lambich.dailyshop.service;

import lambich.dailyshop.beans.Product;
import lambich.dailyshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public Product addProduct(Product product) {
        // Implement validation or additional logic if needed
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long productId, Product updatedProduct) {
        // Implement validation or additional logic if needed
        Product existingProduct = getProductById(productId);
        if (existingProduct != null) {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            // Update other fields as needed
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(Long productId) {
        // Implement validation or additional logic if needed
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> getProductsByCategories(List<Long> selectedCategories){
        return productRepository.findByCategories(selectedCategories);
    }

    @Override
    public void saveProduct(Product product){
        productRepository.save(product);
    }

    @Override
    public List<Product> getProductsByUserId(Long userId) {
        return productRepository.findByUserId(userId);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}
