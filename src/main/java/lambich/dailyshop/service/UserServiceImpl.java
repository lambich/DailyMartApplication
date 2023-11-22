package lambich.dailyshop.service;

import lambich.dailyshop.Addition.CustomerUserDetails;
import lambich.dailyshop.beans.User;
import lambich.dailyshop.beans.Role;
import lambich.dailyshop.repository.RoleRepository;
import lambich.dailyshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private User user;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        return null;
    }


    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public int updateUser(User user) {
        return 0;
    }

    @Override
    public int updateUserWithAvatar(User user) {
        return 0;
    }


    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return null;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        User user = userRepository.findByUsername(username);
        System.out.println("User Name: " + user.getUserName());
        if(user == null)
        {
            throw new UsernameNotFoundException("Invalid email or password.");
        }
        //return new StudentUserDetails(user);
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));

    }


    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles)
    {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public User getCurrentlyLoggedInUser(Authentication authentication) {

        Object principal = null;

        if (authentication != null) {
            principal = authentication.getPrincipal();
        }

        if (principal != null && principal instanceof CustomerUserDetails) {
            CustomerUserDetails userDetails = (CustomerUserDetails) principal;
            return userDetails.getUser();
        }

        return null;

    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public String getUserEncryptedPasswordById(Long id) {
        return null;
    }

    @Override
    public boolean verify(String verificationCode) {
        return false;
    }

    public void register(User user) throws IOException, MessagingException {
        System.out.println("user pass register " + user.getPassword());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);;
        user.setEnabled(true);

        //set role
        long id =1;
        Role role = roleRepository.getById(id);
        user.addRole(role);

        //Set default avatar
        /*File defaultAvatar=new File("src/main/resources/static/img/default_avatar.png");
        String absolutePath = defaultAvatar.getAbsolutePath();
        File a=new File(absolutePath);*/

        userRepository.save(user);
    }


    @Override
    public void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {

    }

    @Override
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

}

