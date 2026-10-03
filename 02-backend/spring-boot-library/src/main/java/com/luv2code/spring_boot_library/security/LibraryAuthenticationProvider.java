package com.luv2code.spring_boot_library.security;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.luv2code.spring_boot_library.dao.LibraryUserRepository;
import com.luv2code.spring_boot_library.service.LibraryUserDetailsService;

@Component
public class LibraryAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired
	private LibraryUserDetailsService libraryUserDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userName = authentication.getName();
		String password = authentication.getCredentials().toString();
		UserDetails userDetails = libraryUserDetailsService.loadUserByUsername(userName);

		if(passwordEncoder.matches(password, userDetails.getPassword())) {
			Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
			return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
		}else {
			throw new BadCredentialsException("Invalid password");

		}
	}

	@Override
	public boolean supports(Class<?> authentication) {

		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
