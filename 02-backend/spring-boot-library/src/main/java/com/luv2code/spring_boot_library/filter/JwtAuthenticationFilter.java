package com.luv2code.spring_boot_library.filter;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.luv2code.spring_boot_library.service.JwtService;
import com.luv2code.spring_boot_library.service.LibraryUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private ApplicationContext context;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		/*String authHeader = request.getHeader("Authorization");
		
		String token = null;
		String userName = null;
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			userName = jwtService.extractUserName(token);
			
		
		}*/

		String token = parseJwtFromCookie(request);
		String userName =null;
		
		if (StringUtils.hasText(token)) {
			try {
                userName = jwtService.extractUserName(token);
            } catch (Exception e) {
                logger.error("Failed to extract username from token: " + e.getMessage());
            }
		}
		
		if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		
			UserDetails userDetails = context.getBean(LibraryUserDetailsService.class).loadUserByUsername(userName);

			if(jwtService.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
		
	}
		
	private String parseJwtFromCookie(HttpServletRequest request) {
		
		Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
            	System.out.println("DEBUG Cookie Name: [" + cookie.getName() + "]");
                if ("accessToken".equals(cookie.getName())) { 
                    return cookie.getValue();
                }
            }
        }
        
     // 2. Fallback to Authorization Header (Bearer token)
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	    return "OPTIONS".equalsIgnoreCase(request.getMethod());
	}
}
