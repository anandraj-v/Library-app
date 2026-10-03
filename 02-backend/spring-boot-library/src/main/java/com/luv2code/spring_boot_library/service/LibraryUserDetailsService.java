package com.luv2code.spring_boot_library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.luv2code.spring_boot_library.dao.LibraryUserRepository;
import com.luv2code.spring_boot_library.model.LibraryUserDetails;

@Service
public class LibraryUserDetailsService implements UserDetailsService{
	
	@Autowired
	private LibraryUserRepository libraryUserRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return libraryUserRepository.findByEmail(username)
				.map(LibraryUserDetails :: new)
				.orElseThrow(()-> new UsernameNotFoundException("User not found"));
	}

}
