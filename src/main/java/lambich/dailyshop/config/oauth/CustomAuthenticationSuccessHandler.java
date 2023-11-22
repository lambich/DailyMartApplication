package lambich.dailyshop.config.oauth;

import lambich.dailyshop.Addition.CustomerUserDetails;
import lambich.dailyshop.beans.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {

		System.out.println("onAuthenticationSuccess: ");
		System.out.println("auth name: " + authentication.getName());
		System.out.println("auth principal " + authentication.getPrincipal());
		System.out.println("auth detail: " + authentication.getDetails());
		System.out.println("is authenticated: " + authentication.isAuthenticated());

		CustomerUserDetails customerUserDetails = (CustomerUserDetails) authentication.getPrincipal();
		User user = customerUserDetails.getUser();
		System.out.println("User email: " + user.getEmail());

		//custom logics
		UrlPathHelper helper = new UrlPathHelper();
		String contextPath = helper.getContextPath(request);
		response.sendRedirect(contextPath);

	}

	}