package com.coolcats.coolcats.service;

import com.coolcats.coolcats.dto.UserDto;
import com.coolcats.coolcats.entity.User;
import com.coolcats.coolcats.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public void saveUser(UserDto userDto)
    {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPasswd(passwordEncoder.encode(userDto.getPasswd()));
        user.setStatus("user");
        userRepository.save(user);
    }
}
