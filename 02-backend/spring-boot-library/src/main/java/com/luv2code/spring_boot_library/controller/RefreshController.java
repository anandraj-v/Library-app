package com.luv2code.spring_boot_library.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.WebUtils;

import com.luv2code.spring_boot_library.dto.LoginResponseDto;
import com.luv2code.spring_boot_library.entity.LibraryUser;
import com.luv2code.spring_boot_library.entity.RefreshToken;
import com.luv2code.spring_boot_library.service.AuthService;
import com.luv2code.spring_boot_library.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class RefreshController {

	@Autowired
	private AuthService authService;

	@Autowired
	private JwtService jwtService;

	@PostMapping("/refresh")
	public ResponseEntity<LoginResponseDto> refreshToken(HttpServletRequest request) {
		String refreshTokenValue = getCookieValue(request, "refreshToken");

		if (refreshTokenValue == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new LoginResponseDto(null, "Refresh token is missing"));
		}

		// Step 1: Extract LibraryUser safely
		Optional<LibraryUser> userOpt = authService.getRefreshTokenRepository().findByToken(refreshTokenValue).map(authService::verifyExpiration)
				.map(RefreshToken::getUser);

		// Step 2: Return response based on user presence
		if (userOpt.isPresent()) {
			LibraryUser user = userOpt.get();
			String newAccessToken = jwtService.generateToken(user.getEmail());
			ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken).httpOnly(true).secure(true)
					.path("/").maxAge(30 * 60).sameSite("Strict").build();

			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessCookie.toString())
					.body(new LoginResponseDto(null, "Token refreshed successfully"));
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new LoginResponseDto(null, "Invalid or expired refresh token"));
	}

	private String getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie cookie = WebUtils.getCookie(request, cookieName);
		return cookie != null ? cookie.getValue() : null;
	}
}
