package com.luv2code.springdemo.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.luv2code.springdemo.entity.User;

//implementing UserAwareUserDetails to avoid infinite recursion (refer below stackoverflow post)
//https://stackoverflow.com/questions/14223649/how-to-implement-auditoraware-with-spring-data-jpa-and-spring-security/14232587#14232587

public class UserAwareUserDetails implements UserDetails {

	private static final long serialVersionUID = 8317433159345782449L;
	private final User user;
	private final Collection<? extends GrantedAuthority> grantedAuthorities;

	public UserAwareUserDetails(User user) {
		this(user, new ArrayList<GrantedAuthority>());
	}

	public UserAwareUserDetails(User user,
			Collection<? extends GrantedAuthority> grantedAuthorities) {
		this.user = user;
		this.grantedAuthorities = grantedAuthorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() { return user.getPassword(); }

	@Override
	public String getUsername() { return user.getUsername(); }

	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

	@Override
	public boolean isAccountNonLocked() { return true; }

	@Override
	public boolean isCredentialsNonExpired() { return true; }

	@Override
	public boolean isEnabled() { return user.getEnabled(); }

	public User getUser() { return user; }
}