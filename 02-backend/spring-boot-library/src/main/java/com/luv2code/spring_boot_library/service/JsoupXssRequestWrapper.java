package com.luv2code.spring_boot_library.service;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class JsoupXssRequestWrapper extends HttpServletRequestWrapper {

	public JsoupXssRequestWrapper(HttpServletRequest request) {
		super(request);

	}

	@Override
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null)
			return null;

		String[] sanitizedValues = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			sanitizedValues[i] = sanitize(values[i]);
		}
		return sanitizedValues;
	}

	@Override
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		return sanitize(value);
	}

	private String sanitize(String input) {
		if (input == null)
			return null;
		// Safelist.none() completely strips all HTML tags and scripts
		return Jsoup.clean(input, Safelist.none());
	}
}
