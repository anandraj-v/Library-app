package com.luv2code.spring_boot_library.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luv2code.spring_boot_library.entity.LibraryUser;
import com.luv2code.spring_boot_library.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

	Optional<RefreshToken> findByToken(String token);
    int deleteByUser(LibraryUser user);
}
