package com.example.histology.service;

import com.example.histology.dto.UserRegistrationDto;
import com.example.histology.model.User;
import com.example.histology.model.UserType;
import com.example.histology.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    public List<User> searchUsers(String searchTerm) {
        String searchTermLike = "%" + searchTerm.toLowerCase() + "%";
        return userRepository.searchUsers(searchTermLike);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByUsernameAndIdNot(String username, Long id) {
        return userRepository.existsByUsernameAndIdNot(username, id);
    }

    public User saveUser(User user) {
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            // Only encode password if it's not already encoded (starts with $2a$)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, String username, String firstName, String lastName, UserType userType, String password) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUserType(userType);
            if (password != null && !password.trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }
            return saveUser(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User registerOrUpdateUser(UserRegistrationDto registrationDto) {
        User user = userRepository.findByUsername(registrationDto.getUsername())
                .map(existingUser -> {
                    existingUser.setFirstName(registrationDto.getFirstName());
                    existingUser.setLastName(registrationDto.getLastName());
                    existingUser.setUpdatedAt(LocalDateTime.now());
                    return existingUser;
                })
                .orElseGet(() -> new User(
                        registrationDto.getUsername(),
                        registrationDto.getFirstName(),
                        registrationDto.getLastName(),
                        UserType.USER
                ));
        return saveUser(user);
    }
    
    public User registerNewUser(UserRegistrationDto registrationDto) {
        if (existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        
        // Create new user with the provided details
        User user = new User(
            registrationDto.getUsername(), 
            registrationDto.getPassword(), 
            registrationDto.getFirstName(), 
            registrationDto.getLastName(), 
            registrationDto.getUserType() != null ? registrationDto.getUserType() : UserType.USER
        );
        
        return saveUser(user);
    }
}
