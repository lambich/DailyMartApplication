package lambich.dailyshop.controller;

import lambich.dailyshop.beans.*;
import lambich.dailyshop.service.CartService;
import lambich.dailyshop.service.OrderService;
import lambich.dailyshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Controller
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public CheckoutController(CartService cartService, OrderService orderService, UserService userService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal Authentication authentication,
                           @RequestParam String shippingAddress,
                           @RequestParam String note) {
        // Retrieve the current user's cart
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Assuming you have a method to get the currently logged-in user
        User user = userService.getCurrentlyLoggedInUser(auth);
        Cart cart = cartService.getCartByUser(user);

        // Create an order
        Order order = new Order();
        order.setUser(user);
        //order.setOrderItems(new Set<>()); // Initialize the order items list

        Set<OrderItem> orderItems = new HashSet<>();;
        // Create order items and associate them with the order
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        // Set the total amount for the order
        order.setTotalAmount(cart.calculateTotalOrder());
        order.setOrderDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        // Set the delivery date to 3 days after the order date
        LocalDateTime deliveryDateTime = LocalDateTime.now().plusDays(3);
        order.setDeliveryDate(Date.from(deliveryDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        order.setNotes(note);
        order.setPaymentMethod("Cash");
        order.setShippingAddress(shippingAddress);
        // Save the order and order items
        orderService.createOrder(order);

        // Remove cart items from the cart
        cartService.clearCart(cart);

        return "redirect:/user/profile";
    }

}

