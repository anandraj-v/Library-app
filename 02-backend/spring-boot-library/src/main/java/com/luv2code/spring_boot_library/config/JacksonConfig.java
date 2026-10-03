package com.luv2code.spring_boot_library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.luv2code.spring_boot_library.service.JsoupJsonDeserializer;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

//@Configuration
public class JacksonConfig {

//	@Bean
//    @Primary
    public ObjectMapper objectMapper() {
		SimpleModule xssModule = new SimpleModule();
        xssModule.addDeserializer(String.class, new JsoupJsonDeserializer());
        
        return JsonMapper.builder()
                .addModule(xssModule)
                .build();
    }
}
