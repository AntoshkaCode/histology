package com.example.histology.repository;

import com.example.histology.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    java.util.List<AppUser> findAllByUserType(com.example.histology.model.UserType userType);
}
