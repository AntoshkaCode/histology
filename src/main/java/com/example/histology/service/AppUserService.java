package com.example.histology.service;

import com.example.histology.dto.UserRegistrationDto;
import com.example.histology.model.AppUser;
import com.example.histology.model.UserType;
import com.example.histology.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AppUser registerNewAppUser(UserRegistrationDto registrationDto) {
        if (appUserRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }
        AppUser user = new AppUser();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setUserType(registrationDto.getUserType() != null ? registrationDto.getUserType() : UserType.USER);
        return appUserRepository.save(user);
    }

    public java.util.List<AppUser> findAll() {
        return appUserRepository.findAll();
    }
}
