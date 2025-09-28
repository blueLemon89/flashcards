package com.flashcards.service;

import com.flashcards.constant.ErrorMessage;
import com.flashcards.dto.UserDto;
import com.flashcards.entity.User;
import com.flashcards.exception.FlashcardExceptions;
import com.flashcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto registerUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new FlashcardExceptions.UserAlreadyExistsException(
                String.format(ErrorMessage.USER_ALREADY_EXISTS_USERNAME, userDto.getUsername()));
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new FlashcardExceptions.UserAlreadyExistsException(
                String.format(ErrorMessage.USER_ALREADY_EXISTS_EMAIL, userDto.getEmail()));
        }

        User user = convertToEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user = userRepository.save(user);

        return convertToDto(user);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new FlashcardExceptions.UserNotFoundException(
                    String.format(ErrorMessage.USER_NOT_FOUND_WITH_ID, id)));
        return convertToDto(user);
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new FlashcardExceptions.UserNotFoundException(
                    String.format(ErrorMessage.USER_NOT_FOUND_WITH_USERNAME, username)));
        return convertToDto(user);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new FlashcardExceptions.UserNotFoundException(
                    String.format(ErrorMessage.USER_NOT_FOUND_WITH_ID, id)));

        existingUser.setFullName(userDto.getFullName());
        existingUser.setDefaultNotificationInterval(userDto.getDefaultNotificationInterval());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        existingUser = userRepository.save(existingUser);
        return convertToDto(existingUser);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setDefaultNotificationInterval(user.getDefaultNotificationInterval());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    private User convertToEntity(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setDefaultNotificationInterval(dto.getDefaultNotificationInterval() != null ?
            dto.getDefaultNotificationInterval() : 60);
        return user;
    }
}