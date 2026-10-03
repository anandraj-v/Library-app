package com.luv2code.spring_boot_library.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private LibraryUser user;

    @Column(nullable = false)
    private Instant expiryDate;

    public RefreshToken() {}

    public RefreshToken(String token, LibraryUser user, Instant expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public Integer getId() {
    		return id; 
    	}
    public String getToken() {
    		return token; 
    	}
    public void setToken(String token) { 
    		this.token = token; 
    	}
    public LibraryUser getUser() { 
    		return user; 
    		}
    public void setUser(LibraryUser user) { 
    		this.user = user;
    		}
    public Instant getExpiryDate() { 
    		return expiryDate; 
    		}
    public void setExpiryDate(Instant expiryDate) { 
    		this.expiryDate = expiryDate;
    	}
}
