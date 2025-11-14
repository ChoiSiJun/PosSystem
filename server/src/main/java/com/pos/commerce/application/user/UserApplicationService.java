package com.pos.commerce.application.user;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pos.commerce.application.user.command.DeleteUserCommand;
import com.pos.commerce.application.user.command.UpdateUserCommand;
import com.pos.commerce.application.user.command.LoginUserCommand;
import com.pos.commerce.application.user.command.RegisterUserCommand;
import com.pos.commerce.application.user.query.GetUserByEmailQuery;
import com.pos.commerce.application.user.query.GetUserByIdQuery;
import com.pos.commerce.application.user.query.GetUserByUsernameQuery;
import com.pos.commerce.domain.user.Role;
import com.pos.commerce.domain.user.User;
import com.pos.commerce.infrastructure.user.Jwt.JwtProvider;
import com.pos.commerce.infrastructure.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserApplicationService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public User createUser(RegisterUserCommand command) {
        if (userRepository.existsByUsername(command.username())) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다: " + command.username());
        }

        Set<Role> roles = command.roles() != null && !command.roles().isEmpty()
                ? command.roles()
                : Set.of(Role.USER);

        boolean enabled = command.enabled() != null ? command.enabled() : true;

        User user = User.builder()
                .username(command.username())
                .password(passwordEncoder.encode(command.password()))
                .email(command.email())
                .roles(roles)
                .enabled(enabled)
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(GetUserByUsernameQuery query) {
        return userRepository.findByUsername(query.username());
    }

    @Override
    public User updateUser(UpdateUserCommand command) {
        User existingUser = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + command.userId()));

        String updatedPassword = command.password() != null && !command.password().isEmpty()
                ? passwordEncoder.encode(command.password())
                : existingUser.getPassword();

        Set<Role> roles = command.roles() != null && !command.roles().isEmpty()
                ? command.roles()
                : existingUser.getRoles();

        Boolean enabled = command.enabled() != null ? command.enabled() : existingUser.getEnabled();

        User updatedUser = User.builder()
                .id(existingUser.getId())
                .username(existingUser.getUsername())
                .password(updatedPassword)
                .email(command.email() != null ? command.email() : existingUser.getEmail())
                .roles(roles)
                .enabled(enabled)
                .createdAt(existingUser.getCreatedAt())
                .build();

        return userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(DeleteUserCommand command) {
        Long userId = command.userId();
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public String authenticate(LoginUserCommand command) {
        User user = userRepository.findByUsername(command.username())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + command.username()));

        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        return jwtProvider.generateToken(user.getUsername());
    }
}

