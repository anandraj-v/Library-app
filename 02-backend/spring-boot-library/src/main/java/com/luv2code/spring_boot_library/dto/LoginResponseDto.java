package com.luv2code.spring_boot_library.dto;

public class LoginResponseDto {

	//private Long id;
    private String email;
    //private String token;
    private String message;

    public LoginResponseDto( String email, String message) {
        //this.id = id;
        this.email = email;
       // this.token = token;
        this.message = message;
    }

    //public Long getId() { return id; }
    public String getEmail() { return email; }
    //public String getToken() { return token; }
    public String getMessage() { return message; }
}
