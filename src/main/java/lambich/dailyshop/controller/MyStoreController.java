package lambich.dailyshop.controller;

import lambich.dailyshop.beans.Category;
import lambich.dailyshop.beans.Product;
import lambich.dailyshop.beans.User;
import lambich.dailyshop.service.CartService;
import lambich.dailyshop.service.CategoryService;
import lambich.dailyshop.service.ProductService;
import lambich.dailyshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/my-store")
public class MyStoreController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public String showMyStore(Model model, @AuthenticationPrincipal Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);

        List<Category> categories = categoryService.getAllCategories();

        List<Product> products = productService.getProductsByUserId(user.getId());

        //Count item in cart
        int cartItemCount = cartService.getItemNumberInCart(user);

        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("cartItemCount", cartItemCount);

        return "my-store";
    }

    @PostMapping("/add-product")
    public String addProductToMarket(@RequestParam String productName,
                                     @RequestParam String productDescription,
                                     @RequestParam double productPrice,
                                     @RequestParam int productQuantity,
                                     @RequestParam String supplierName,
                                     @RequestParam Long categoryId,
                                     @RequestParam String productImageUrl,
                                     @AuthenticationPrincipal Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);

        // Retrieve the selected category from the database
        Category category = categoryService.getCategoryById(categoryId);

        // Create a new Product object
        Product newProduct = new Product();
        newProduct.setName(productName);
        newProduct.setDescription(productDescription);
        newProduct.setPrice(productPrice);
        newProduct.setQuantity(productQuantity);
        newProduct.setSupplierName(supplierName);
        newProduct.setCategory(category);
        newProduct.setImageUrl(productImageUrl);
        newProduct.setUser(user);

        // Save the product to the market
        productService.saveProduct(newProduct);

        // Redirect back to the my-store page
        return "redirect:/my-store";
    }


}
