package com.taira.cntl.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.taira.cntl.entity.User;
import com.taira.cntl.persistent.UserRepository;
import com.taira.cntl.util.StringUtil;

@Component
public class CustomApiAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        
        User u = userRepo.findByEmail(username);
        if(u!=null) {
	        User userDto = new User();
	        userDto.setEmail(u.getEmail());
	        userDto.setId(u.getId());
	        
	        if(u.getPassword().equals(StringUtil.getHashValue(password, "MD5"))) {
				Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
				
				return new UsernamePasswordAuthenticationToken(userDto, userDto, grantedAuthorities);
	        } else {
	        	throw new BadCredentialsException("Wrong password");
	        }
        }
        throw new UsernameNotFoundException("Cannot find user " + username);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
