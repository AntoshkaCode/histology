package com.example.histology.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthStatusController {
    private final com.example.histology.repository.AppUserRepository appUserRepository;

    public AuthStatusController(com.example.histology.repository.AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/api/auth/status")
    public Map<String, Object> authStatus() {
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AuthStatusController.class);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("[AuthStatus] Authentication: {}", authentication);
        if (authentication != null) {
            logger.info("[AuthStatus] Principal: {}", authentication.getPrincipal());
            logger.info("[AuthStatus] Authorities: {}", authentication.getAuthorities());
            logger.info("[AuthStatus] Is Authenticated: {}", authentication.isAuthenticated());
        }
        Map<String, Object> response = new HashMap<>();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            response.put("authenticated", true);
            String email = authentication.getName();
            response.put("email", email);
            // Try AppUser first
            boolean found = false;
            var appUserOpt = appUserRepository.findByEmail(email);
            if (appUserOpt.isPresent()) {
                var u = appUserOpt.get();
                response.put("firstName", u.getFirstName());
                response.put("position", u.getUserType() != null ? u.getUserType().name() : null);
                logger.info("[AuthStatus] Found AppUser: {} {}", u.getFirstName(), u.getUserType());
            }
            // Optionally: fallback to User entity if needed (pseudo-code, implement if UserRepository exists)
            // if (!found) {
            //     var userOpt = userRepository.findByEmail(email);
            //     if (userOpt.isPresent()) {
            //         var user = userOpt.get();
            //         response.put("firstName", user.getFirstName());
            //         response.put("position", user.getUserType() != null ? user.getUserType().name() : null);
            //         logger.info("[AuthStatus] Fallback User: {} {} {}", user.getFirstName(), user.getEmail(), user.getUserType());
            //     }
            // }
        } else {
            response.put("authenticated", false);
        }
        return response;
    }
}
