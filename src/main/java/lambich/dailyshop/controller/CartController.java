package lambich.dailyshop.controller;

import lambich.dailyshop.beans.Cart;
import lambich.dailyshop.beans.User;
import lambich.dailyshop.service.CartService;
import lambich.dailyshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/view")
    public String viewCart(Model model, @AuthenticationPrincipal Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Assuming you have a method to get the currently logged-in user
        User user = userService.getCurrentlyLoggedInUser(auth);

        Cart newcart = user.getCart();
        if (newcart == null) {
            newcart = new Cart();
            newcart.setUser(user);
            user.setCart(newcart);

            cartService.saveCart(newcart);
        }

        // Retrieve the cart for the current user from the cartService
        Cart cart = cartService.getCartByUser(user);

        //Count item in cart
        int cartItemCount = cartService.getItemNumberInCart(user);

        // Add the cart and its items to the model
        model.addAttribute("cart", cart);
        model.addAttribute("cartItemCount", cartItemCount);

        // Return the view name (cart.html)
        return "cart";
    }
}

