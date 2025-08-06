package com.example.histology.repository;

import com.example.histology.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT a FROM AppUser a WHERE " +
        "CAST(a.id AS string) LIKE CONCAT('%', :search, '%') OR " +
        "LOWER(a.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
        "LOWER(a.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
        "LOWER(a.userType) LIKE LOWER(CONCAT('%', :search, '%'))")
    java.util.List<AppUser> searchAppUsers(@org.springframework.data.repository.query.Param("search") String search);
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    java.util.List<AppUser> findAllByUserType(com.example.histology.model.UserType userType);
}
