package com.luv2code.spring_boot_library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.luv2code.spring_boot_library.dao.LibraryUserRepository;
import com.luv2code.spring_boot_library.dto.LoginRequestDto;
import com.luv2code.spring_boot_library.entity.LibraryUser;

@Service
public class UserService {

	@Autowired
	private LibraryUserRepository repo;
	
	@Autowired
	private JwtService jwtService;
	
	
	@Autowired
	AuthenticationManager authManager;

	public String verify(LoginRequestDto user) {
		Authentication authentication= authManager.authenticate(
				new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
		if(authentication.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return jwtService.generateToken(user.getEmail());
		}
		return "fail";
	}
}
