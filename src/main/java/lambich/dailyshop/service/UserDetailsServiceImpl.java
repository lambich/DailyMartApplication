package lambich.dailyshop.service;

import lambich.dailyshop.Addition.CustomerUserDetails;
import lambich.dailyshop.beans.User;
import lambich.dailyshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        System.out.println("email in student service " + user.getUserName());
        if(user == null)
        {
            System.out.println("Could not find user with email: " + username);
            throw new UsernameNotFoundException("Could not find user with email: " + username);
        }
        else{
            System.out.println("user is not null");
        }
        return new CustomerUserDetails(user);
    }
}
