package lambich.dailyshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout() {
        // Perform any logout-related operations here
        // You might want to invalidate the session, clear authentication, etc.

        return "redirect:/login?logout"; // Redirect to the login page with a logout parameter
    }
}

