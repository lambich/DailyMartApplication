package lambich.dailyshop.controller;

import lambich.dailyshop.beans.Order;
import lambich.dailyshop.beans.User;
import lambich.dailyshop.service.CartService;
import lambich.dailyshop.service.OrderService;
import lambich.dailyshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final CartService cartService;

    @Autowired
    public UserController(UserService userService, OrderService orderService, CartService cartService) {
        this.userService = userService;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/profile")
    public String viewUserProfile(Model model, @AuthenticationPrincipal Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            User user = userService.getCurrentlyLoggedInUser(auth);
            List<Order> orders = orderService.getAllOrdersByUser(user.getId());
            //Count item in cart
            int cartItemCount = cartService.getItemNumberInCart(user);
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("userOrders", orders);
                model.addAttribute("cartItemCount", cartItemCount);
                return "profile";
            }
        }

        // If the user is not authenticated or the user is not found, you might redirect to an error page or login page.
        return "redirect:/login";
    }
}
