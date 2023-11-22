package lambich.dailyshop.service;

import lambich.dailyshop.beans.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public interface UserService extends UserDetailsService {

	//User save(UserRegistrationDto registrationDto) throws IOException;

	List<User> getAllUsers();

	User saveUser(User user);

	User getUserById(Long id);

	int updateUser(User user);

	int updateUserWithAvatar(User user);

	void deleteUserById(Long id);


	UserDetails loadUserByEmail(String email) throws UsernameNotFoundException;

	User getCurrentlyLoggedInUser(Authentication authentication);

	User getUserByEmail(String email);

	String getUserEncryptedPasswordById(Long id);

	//public  User getCurrentlyLoggedInUser(Authentication authentication);
	public boolean verify(String verificationCode);
	void register(User user) throws IOException, MessagingException;

	void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;

	User findByUsername(String username);
}