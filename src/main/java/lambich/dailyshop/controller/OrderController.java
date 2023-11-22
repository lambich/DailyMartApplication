package lambich.dailyshop.controller;

import lambich.dailyshop.beans.Order;
import lambich.dailyshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/list")
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/list";
    }

    @GetMapping("/{orderId}")
    public String viewOrder(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order/view";
    }
    @PostMapping("/confirm-receipt/{orderId}")
    public String confirmReceipt(@PathVariable Long orderId) {
        // Logic to update order status (e.g., set status to "Received")
        orderService.confirmOrderReceipt(orderId);

        // Redirect to the order detail page or any other appropriate page
        return "redirect:/user/profile";
    }
}

