package com.luv2code.spring_boot_library.controller;

import java.time.LocalDateTime;
import java.util.Date;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.spring_boot_library.dao.LibraryUserRepository;
import com.luv2code.spring_boot_library.entity.LibraryUser;
import com.luv2code.spring_boot_library.service.LibraryUserDetailsService;

@RestController
public class RegisterController {

	@Autowired
	private LibraryUserRepository libraryUserRepository;
	
	@Autowired
	private LibraryUserDetailsService libraryUserDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<String> userRegister(@RequestBody LibraryUser libraryUser) {
		LibraryUser savedLibraryUser = null;
		UserDetails userDetails = null;
		ResponseEntity<String> response = null;
		
		try {
	    userDetails = libraryUserDetailsService.loadUserByUsername(libraryUser.getEmail());
		
		}catch(UsernameNotFoundException e) {

		}
		try {
			@Nullable
			String hashPassword = passwordEncoder.encode(libraryUser.getPassword());
			libraryUser.setPassword(hashPassword);
			libraryUser.setCreatedDate(LocalDateTime.now().toString());
			
			if(userDetails == null) {
			savedLibraryUser = libraryUserRepository.save(libraryUser);
			}else {
				 response = ResponseEntity.status(HttpStatus.CONFLICT)
				.body("A user with this email already exists");
				
			}
			
			if (savedLibraryUser != null && savedLibraryUser.getUserId() > 0) {
				 response = ResponseEntity.status(HttpStatus.CREATED)
						.body("Given user details are sucessfully registered");
			}
		} catch (Exception ex) {
			ex.printStackTrace(); 
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An unexpected error occurred: " + ex.getMessage());
		}
		
		return  response;
	}

}
