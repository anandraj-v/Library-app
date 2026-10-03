package com.luv2code.spring_boot_library.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.luv2code.spring_boot_library.dao.LibraryUserRepository;
import com.luv2code.spring_boot_library.dao.RefreshTokenRepository;
import com.luv2code.spring_boot_library.entity.LibraryUser;
import com.luv2code.spring_boot_library.entity.RefreshToken;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

	
	private final RefreshTokenRepository refreshTokenRepository;
    private final LibraryUserRepository userRepository;
    private final JwtService jwtService; // Your existing JWT utility class

    @Value("${app.jwt.refreshExpirationMs:604800000}") // 7 days in ms
    private Long refreshTokenDurationMs;

    public AuthService(RefreshTokenRepository refreshTokenRepository, LibraryUserRepository userRepository, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public RefreshToken createRefreshToken(String email) {
        LibraryUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        // Delete old refresh token if exists
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                user,
                Instant.now().plusMillis(refreshTokenDurationMs)
        );

        return refreshTokenRepository.save(refreshToken);
    }
    
    public RefreshTokenRepository getRefreshTokenRepository() {
        return this.refreshTokenRepository;
    }
    
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token has expired. Please log in again.");
        }
        return token;
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshTokenRepository::delete);
    }
}
