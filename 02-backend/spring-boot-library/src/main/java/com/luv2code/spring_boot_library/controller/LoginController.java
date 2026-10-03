package com.luv2code.spring_boot_library.controller;


import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.spring_boot_library.dto.LoginRequestDto;
import com.luv2code.spring_boot_library.dto.LoginResponseDto;
import com.luv2code.spring_boot_library.entity.LibraryUser;
import com.luv2code.spring_boot_library.entity.RefreshToken;
import com.luv2code.spring_boot_library.service.AuthService;
import com.luv2code.spring_boot_library.service.LibraryUserDetailsService;
import com.luv2code.spring_boot_library.service.UserService;

@RestController
public class LoginController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LibraryUserDetailsService libraryUserDetailsService;

	@Autowired
	private UserService service;
	
	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> userLogin(@RequestBody LoginRequestDto libraryUser) {

		
		try {
			
			String jwtToken = service.verify(libraryUser);
			
			if ("fail".equals(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponseDto(null, "Invalid Credentials"));
            }
			String rawPassword = libraryUser.getPassword();

			UserDetails userDetails = libraryUserDetailsService.loadUserByUsername(libraryUser.getEmail());
			
			RefreshToken refreshToken = authService.createRefreshToken(libraryUser.getEmail());
				
				ResponseCookie cookie = ResponseCookie.from("accessToken", jwtToken)
										.httpOnly(true)
										.secure(false)
										.path("/")
										.maxAge(24 * 60 * 60)
										.sameSite("Lax")
										.build();
				ResponseCookie refreshCookie =  ResponseCookie.from("refreshToken", refreshToken.getToken())
										.httpOnly(true)
										.secure(false)
										.path("/")
										.maxAge(7 * 24 * 60 * 60)
										.sameSite("Strict")
										.build();
						
				LoginResponseDto userResponse = new LoginResponseDto(userDetails.getUsername(),
						"Login Succesfully");

				return ResponseEntity.status(HttpStatus.OK)
						.header(HttpHeaders.SET_COOKIE, cookie.toString())
						.header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
						.body(userResponse);
			
		} catch (UsernameNotFoundException ex) {
			 
			LoginResponseDto erroeResponse = new LoginResponseDto(null, ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroeResponse);
		}

	}

	@GetMapping("/me")
	public ResponseEntity<LoginResponseDto> currentUser(Authentication authentication) {
		
		if (authentication == null || !authentication.isAuthenticated()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
		
		LoginResponseDto userResponse = new LoginResponseDto(authentication.getName(), "Authenticated");

	    return ResponseEntity.status(HttpStatus.OK).body(userResponse);
	}
}
