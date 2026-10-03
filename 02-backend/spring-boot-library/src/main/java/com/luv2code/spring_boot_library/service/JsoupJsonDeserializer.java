package com.luv2code.spring_boot_library.service;



import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import org.springframework.boot.jackson.JacksonComponent;
@JacksonComponent
public class JsoupJsonDeserializer extends ValueDeserializer<String> {

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
		String value = p.getValueAsString();
        if (value == null) {
            return null;
        }
	 return Jsoup.clean(value, Safelist.none());
	}

}
