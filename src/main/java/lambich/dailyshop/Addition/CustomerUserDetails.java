package lambich.dailyshop.Addition;

import lambich.dailyshop.beans.User;
import lambich.dailyshop.beans.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class CustomerUserDetails implements org.springframework.security.core.userdetails.UserDetails {

	private User user;

	public CustomerUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = (Set<Role>) user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
         
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	public String getEmail() {
		return user.getEmail();
	}

	public User getUser() {
		return this.user;
	}
	

	/*@Override
	public String getUsername() {
		return user.getFirstName() + " " + user.getLastName();
	}*/
	@Override
	public String getUsername() {
		return user.getUserName();
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

}
