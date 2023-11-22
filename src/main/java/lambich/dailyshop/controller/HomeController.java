package lambich.dailyshop.controller;

import lambich.dailyshop.beans.*;
import lambich.dailyshop.repository.UserRepository;
import lambich.dailyshop.service.CartService;
import lambich.dailyshop.service.CategoryService;
import lambich.dailyshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {

	private final UserRepository userRepo;
	private final CategoryService categoryService;
	private final ProductService productService;
	private final CartService cartService;


	@Autowired
	public HomeController(UserRepository userRepo, CategoryService categoryService, ProductService productService, CartService cartService) {
		this.userRepo = userRepo;
		this.categoryService = categoryService;
		this.productService = productService;
		this.cartService = cartService;
	}
	@GetMapping("/")
	public String goHome(Model model, @AuthenticationPrincipal Authentication authentication,
						 @RequestParam(name = "selectedCategories", required = false) List<Long> selectedCategories,
						 @RequestParam(name = "searchKeyword", required = false) String searchKeyword) throws
			IOException {

		//-------------------------------------------Authentication
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
			System.out.println("User : " + auth.getName());
			User user = userRepo.findByUsername(auth.getName());

			//for category
			List<Category> categories = categoryService.getAllCategories();

			// Fetch products based on selected categories
			List<Product> products;

			/*if (selectedCategories != null && !selectedCategories.isEmpty()) {
				// Filter products based on selected categories
				products = productService.getProductsByCategories(selectedCategories);
			} else {
				// If no categories are selected, fetch all products
				products = productService.getAllProducts();
			}*/

			// If searchKeyword is provided, search by name
			if (searchKeyword != null && !searchKeyword.isEmpty()) {
				products = productService.searchProducts(searchKeyword);
			} else if (selectedCategories != null && !selectedCategories.isEmpty()) {
				// Filter products based on selected categories
				products = productService.getProductsByCategories(selectedCategories);
			} else {
				// If no categories are selected and no search keyword, fetch all products
				products = productService.getAllProducts();
			}

			//Count item in cart
			int cartItemCount = cartService.getItemNumberInCart(user);
			Cart cart = cartService.getCartByUser(user);

			// Check if each product is in the cart and add a flag to the model
			for (Product product : products) {
				boolean inCart = cartService.isProductInCart(product.getId(), user.getUserName());
				product.setInCurrentUserCart(inCart);
			}

			if(user!=null) {
				model.addAttribute("user", user.getUserName());
				model.addAttribute("userName", user.getUserName());
				model.addAttribute("email", user.getEmail());
				model.addAttribute("categories", categories);
				model.addAttribute("products", products);
				model.addAttribute("cartItemCount", cartItemCount);
				model.addAttribute("cart", cart);
			}
			else {
				System.out.println("Fail to load user");
				return "login.html";
			}
			// Rest of your code handling the user...
		} else {
			// Handle the case when there is no authenticated user
			// You might redirect to a login page or perform other actions
			System.out.println("No authenticated user");
			return "login.html";
		}
		return "dashboard.html";
	}


}