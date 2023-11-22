package lambich.dailyshop.config.oauth;

import lambich.dailyshop.beans.User;
import lambich.dailyshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final UserService userService;

    @Autowired
    public CustomAuthenticationFailureHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");

        String redirectURL = "/login?error&username=" + username;

        if (exception.getMessage().contains("OTP")) {
            redirectURL = "/login?otp=true&username=" + username;
        } else {
            UserDetails user = userService.loadUserByUsername(username);
            if (user != null && user.isEnabled()) {
                redirectURL = "/login?otp=true&username=" + username;
            }

        }

        super.setDefaultFailureUrl(redirectURL);


        super.onAuthenticationFailure(request, response, exception);
    }
}
