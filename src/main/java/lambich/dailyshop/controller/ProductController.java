package lambich.dailyshop.controller;

import lambich.dailyshop.beans.Cart;
import lambich.dailyshop.beans.CartItem;
import lambich.dailyshop.beans.Product;
import lambich.dailyshop.beans.User;
import lambich.dailyshop.service.CartService;
import lambich.dailyshop.service.ProductService;
import lambich.dailyshop.service.UserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;


    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product addedProduct = productService.addProduct(product);
        return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        Product product = productService.updateProduct(productId, updatedProduct);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{productId}")
    public String getProductDetail(@PathVariable Long productId, Model model, Principal principal) {
        Product product = productService.getProductById(productId);
        User user = userService.getUserById(product.getUser().getId());

        // Check if the product is in the user's cart
        boolean productInCart = false;
        if (principal != null) {
            String username = principal.getName();
            productInCart = cartService.isProductInCart(productId, username);
        }

        //Count item in cart
        int cartItemCount = cartService.getItemNumberInCart(user);

        model.addAttribute("product", product);
        model.addAttribute("user", user);
        model.addAttribute("productInCart", productInCart);
        model.addAttribute("cartItemCount", cartItemCount);

        return "product-detail";
    }

    @PostMapping("/add-to-cart/{productId}")
    public String addToCart(@PathVariable Long productId, @RequestParam int quantity, @AuthenticationPrincipal Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getCurrentlyLoggedInUser(auth); // Implement getCurrentUser() based on your authentication mechanism

        Product product = productService.getProductById(productId);

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
        }
        System.out.println("Cart: " + cart);

        // Check if the product is already in the cart
        CartItem existingCartItem = cartService.findCartItemByProductAndUser(product, user);

        if (existingCartItem != null) {
            // Update the quantity if the product is already in the cart
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        } else {
            // If the product is not in the cart, create a new CartItem
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(user.getCart()); // Assuming user.getCart() returns the user's cart
            cartItem.setQuantity(quantity);

            // Add the new CartItem to the cart
            cartService.addCartItem(cartItem);
        }

        // Update the database
        //cartService.saveCart(cart);

        return "redirect:/api/products/"+product.getId();
        //return new ResponseEntity<>("Product added to cart successfully", HttpStatus.OK);
    }

    @PostMapping("/add-to-cart-from-dashboard/{productId}")
    public String addToCartFromDasboard(@PathVariable Long productId, @RequestParam int quantity, @AuthenticationPrincipal Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getCurrentlyLoggedInUser(auth); // Implement getCurrentUser() based on your authentication mechanism

        Product product = productService.getProductById(productId);

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
        }
        System.out.println("Cart: " + cart);

        // Check if the product is already in the cart
        CartItem existingCartItem = cartService.findCartItemByProductAndUser(product, user);

        if (existingCartItem != null) {
            // Update the quantity if the product is already in the cart
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        } else {
            // If the product is not in the cart, create a new CartItem
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(user.getCart()); // Assuming user.getCart() returns the user's cart
            cartItem.setQuantity(quantity);

            // Add the new CartItem to the cart
            cartService.addCartItem(cartItem);
        }

        // Update the database
        //cartService.saveCart(cart);

        return "redirect:/";
        //return new ResponseEntity<>("Product added to cart successfully", HttpStatus.OK);
    }

    @PostMapping("/remove-from-cart/{productId}")
    public String removeFromCart(@PathVariable Long productId, @AuthenticationPrincipal Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getCurrentlyLoggedInUser(auth);
        Product product = productService.getProductById(productId);

        // Ensure that the user and cart are not null
        if (user != null && user.getCart() != null) {
            // Find the CartItem based on product and user
            CartItem cartItemToRemove = cartService.findCartItemByProductAndUser(product, user);

            // Remove the cart item from the set and update bidirectional relationship
            if (cartItemToRemove != null) {
                //user.getCart().getCartItems().remove(cartItemToRemove);
                //cartItemToRemove.removeFromCart(); // Update bidirectional relationship
                cartService.removeCartItem(cartItemToRemove);
                // Update the database
                //cartService.saveCart(user.getCart());
            }
        }

        return "redirect:/api/products/" + product.getId();
    }

    @PostMapping("/remove-from-cart-from-dashboard/{productId}")
    public String removeFromCartFromDashboard(@PathVariable Long productId, @AuthenticationPrincipal Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getCurrentlyLoggedInUser(auth);
        Product product = productService.getProductById(productId);

        // Ensure that the user and cart are not null
        if (user != null && user.getCart() != null) {
            // Find the CartItem based on product and user
            CartItem cartItemToRemove = cartService.findCartItemByProductAndUser(product, user);

            // Remove the cart item from the set and update bidirectional relationship
            if (cartItemToRemove != null) {
                //user.getCart().getCartItems().remove(cartItemToRemove);
                //cartItemToRemove.removeFromCart(); // Update bidirectional relationship
                cartService.removeCartItem(cartItemToRemove);
                // Update the database
                //cartService.saveCart(user.getCart());
            }
        }

        return "redirect:/";
    }

    @PostMapping("/remove-in-cart/{productId}")
    public String removeInCart(@PathVariable Long productId, @AuthenticationPrincipal Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getCurrentlyLoggedInUser(auth);
        Product product = productService.getProductById(productId);

        // Ensure that the user and cart are not null
        if (user != null && user.getCart() != null) {
            // Find the CartItem based on product and user
            CartItem cartItemToRemove = cartService.findCartItemByProductAndUser(product, user);

            // Remove the cart item from the set and update bidirectional relationship
            if (cartItemToRemove != null) {
                //user.getCart().getCartItems().remove(cartItemToRemove);
                //cartItemToRemove.removeFromCart(); // Update bidirectional relationship
                cartService.removeCartItem(cartItemToRemove);
                // Update the database
                //cartService.saveCart(user.getCart());
            }
        }

        return "redirect:/cart/view";
    }



}


