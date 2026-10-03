package com.luv2code.spring_boot_library.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.luv2code.spring_boot_library.service.JsoupXssRequestWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JsoupXssFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		JsoupXssRequestWrapper wrappedRequest = new JsoupXssRequestWrapper(request);
        filterChain.doFilter(wrappedRequest, response);
		
	}

}
