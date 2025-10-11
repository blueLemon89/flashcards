package com.flashcards.service;

import com.flashcards.constant.ErrorMessage;
import com.flashcards.dto.UserDto;
import com.flashcards.entity.User;
import com.flashcards.exception.FlashcardExceptions;
import com.flashcards.mapper.UserMapper;
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
    private final UserMapper userMapper;

    public UserDto registerUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new FlashcardExceptions.UserAlreadyExistsException(
                String.format(ErrorMessage.USER_ALREADY_EXISTS_USERNAME, userDto.getUsername()));
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new FlashcardExceptions.UserAlreadyExistsException(
                String.format(ErrorMessage.USER_ALREADY_EXISTS_EMAIL, userDto.getEmail()));
        }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getDefaultNotificationInterval() == null) {
            user.setDefaultNotificationInterval(60);
        }
        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new FlashcardExceptions.UserNotFoundException(
                    String.format(ErrorMessage.USER_NOT_FOUND_WITH_ID, id)));
        return userMapper.toDto(user);
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new FlashcardExceptions.UserNotFoundException(
                    String.format(ErrorMessage.USER_NOT_FOUND_WITH_USERNAME, username)));
        return userMapper.toDto(user);
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
        return userMapper.toDto(existingUser);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toDto).collect(Collectors.toList());
    }
}