package com.luv2code.spring_boot_library.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luv2code.spring_boot_library.entity.LibraryUser;

@Repository
public interface LibraryUserRepository extends JpaRepository<LibraryUser, Long>{

	Optional<LibraryUser> findByEmail(String email);
}
