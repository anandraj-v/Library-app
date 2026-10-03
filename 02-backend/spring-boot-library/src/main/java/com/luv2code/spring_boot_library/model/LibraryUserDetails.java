package com.luv2code.spring_boot_library.model;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.luv2code.spring_boot_library.entity.LibraryUser;

public class LibraryUserDetails implements UserDetails{
	
	private LibraryUser libraryUser;
	
	public LibraryUserDetails(LibraryUser libraryUser) {
		this.libraryUser =  libraryUser;
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(libraryUser.getRole()));
		return authorities;
	}

	@Override
	public @Nullable String getPassword() {
		return libraryUser.getPassword();
	}

	@Override
	public String getUsername() {
		return libraryUser.getEmail();
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
	public  boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
}
