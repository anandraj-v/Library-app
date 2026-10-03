package com.luv2code.spring_boot_library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.luv2code.spring_boot_library.dto.LoginResponseDto;
import com.luv2code.spring_boot_library.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
@RestController
public class LogOutController {

	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/api/auth/logout")
    public ResponseEntity<LoginResponseDto> logout(HttpServletRequest request) {
		
		System.out.println("=== LOGOUT CONTROLLER HIT ===");
       
		String refreshTokenValue = getCookieValue(request, "refreshToken");
        if (refreshTokenValue != null) {
            authService.deleteRefreshToken(refreshTokenValue);
            SecurityContextHolder.clearContext();
        }

        // Clear cookies by setting maxAge = 0
        ResponseCookie cleanAccessCookie = createCookie("accessToken", "", 0, "/");
        ResponseCookie cleanRefreshCookie = createCookie("refreshToken", "", 0, "/");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanAccessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, cleanRefreshCookie.toString())
                .body(new LoginResponseDto(null,"Logged out successfully"));
    }

    // Helper method to construct HttpOnly cookies
    private ResponseCookie createCookie(String name, String value, long maxAgeSeconds, String path) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false) // Enforces HTTPS
                .sameSite("Lax")
                .path(path)
                .maxAge(maxAgeSeconds)
                .build();
    }

    // Helper method to extract cookie by name
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        var cookie = WebUtils.getCookie(request, cookieName);
        return cookie != null ? cookie.getValue() : null;
    }
}
