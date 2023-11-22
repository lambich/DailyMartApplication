package lambich.dailyshop.controller;


import lambich.dailyshop.beans.User;
import lambich.dailyshop.repository.UserRepository;
import lambich.dailyshop.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Controller
@AllArgsConstructor
//@RequestMapping("/registration")
public class RegistrationController {

    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        System.out.println("Go to Registration");
        return "registration"; // Check that this view name is correct
    }

    @PostMapping("/process_register")
    public String processRegister(Model model, User user, HttpServletRequest request)
            throws IOException, MessagingException {
        System.out.println("getSiteURL: " + getSiteURL(request));
        userService.register(user);
        model.addAttribute("pageTitle","Registration Succeeded!");
        return "login";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

}
