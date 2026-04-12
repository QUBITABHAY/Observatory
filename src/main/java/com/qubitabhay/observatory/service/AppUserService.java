package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.dto.user.CreateUserRequest;
import com.qubitabhay.observatory.dto.user.UpdateUserRoleRequest;
import com.qubitabhay.observatory.dto.user.UserResponse;
import com.qubitabhay.observatory.model.AppUser;
import com.qubitabhay.observatory.model.UserRole;
import com.qubitabhay.observatory.repository.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (appUserRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("User already exists with username: " + request.username());
        }

        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        return toResponse(appUserRepository.save(user));
    }

    public List<UserResponse> list() {
        return appUserRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse get(Long id) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        return toResponse(user);
    }

    @Transactional
    public UserResponse updateRole(Long id, UpdateUserRoleRequest request) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        user.setRole(request.role());
        return toResponse(appUserRepository.save(user));
    }

    @Transactional
    public void bootstrapAdminIfMissing(String username, String rawPassword) {
        if (appUserRepository.existsByUsername(username)) {
            return;
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(UserRole.ADMIN);
        appUserRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                .build();
    }

    private UserResponse toResponse(AppUser user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
