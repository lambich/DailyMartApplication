package lambich.dailyshop.service;

// CategoryService.java
import lambich.dailyshop.beans.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category getCategoryById(Long id);
}

