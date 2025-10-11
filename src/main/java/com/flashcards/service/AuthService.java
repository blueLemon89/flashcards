package com.flashcards.service;

import com.flashcards.constant.ErrorMessage;
import com.flashcards.dto.auth.*;
import com.flashcards.entity.PasswordResetToken;
import com.flashcards.entity.RefreshToken;
import com.flashcards.entity.User;
import com.flashcards.exception.FlashcardExceptions;
import com.flashcards.repository.PasswordResetTokenRepository;
import com.flashcards.repository.RefreshTokenRepository;
import com.flashcards.repository.UserRepository;
import com.flashcards.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshTokenDuration;

    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new FlashcardExceptions.UserAlreadyExistsException(
                    String.format(ErrorMessage.USER_ALREADY_EXISTS_EMAIL, request.getEmail()));
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new FlashcardExceptions.UserAlreadyExistsException(
                    String.format(ErrorMessage.USER_ALREADY_EXISTS_USERNAME, request.getUsername()));
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setProvider(User.AuthProvider.LOCAL);
        user.setIsEmailVerified(false);

        user = userRepository.save(user);

        // Send welcome email
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getUsername());
        } catch (Exception e) {
            log.error("Failed to send welcome email", e);
        }

        // Generate tokens
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new FlashcardExceptions.UserNotFoundException(
                        String.format(ErrorMessage.USER_NOT_FOUND, request.getEmail())));

        // Generate tokens
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        // Find refresh token
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        // Validate refresh token
        if (refreshToken.getIsRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }
        if (refreshToken.isExpired()) {
            throw new RuntimeException("Refresh token has expired");
        }

        // Generate new access token
        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken.getUser().getEmail());
        String accessToken = jwtUtil.generateAccessToken(userDetails);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .userId(refreshToken.getUser().getId())
                .username(refreshToken.getUser().getUsername())
                .email(refreshToken.getUser().getEmail())
                .avatarUrl(refreshToken.getUser().getAvatarUrl())
                .build();
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new FlashcardExceptions.UserNotFoundException(
                        String.format(ErrorMessage.USER_NOT_FOUND, request.getEmail())));

        // Delete old password reset tokens
        passwordResetTokenRepository.deleteByUser(user);

        // Create new password reset token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        passwordResetTokenRepository.save(resetToken);

        // Send password reset email
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

        if (resetToken.getIsUsed()) {
            throw new RuntimeException("Password reset token has already been used");
        }
        if (resetToken.isExpired()) {
            throw new RuntimeException("Password reset token has expired");
        }

        // Update user password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Mark token as used
        resetToken.setIsUsed(true);
        passwordResetTokenRepository.save(resetToken);

        // Revoke all refresh tokens for security
        refreshTokenRepository.deleteByUser(user);
    }

    public void logout(String refreshTokenString) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        refreshToken.setIsRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    private String createRefreshToken(User user) {
        // Revoke old refresh tokens
        refreshTokenRepository.findByUserAndIsRevokedFalse(user).ifPresent(oldToken -> {
            oldToken.setIsRevoked(true);
            refreshTokenRepository.save(oldToken);
        });

        // Create new refresh token
        String tokenValue = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(tokenValue);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(refreshTokenDuration / 1000));
        refreshTokenRepository.save(refreshToken);

        return tokenValue;
    }
}
