package com.coolcats.coolcats.controller;

import com.coolcats.coolcats.dto.UserDto;
import com.coolcats.coolcats.entity.User;
import com.coolcats.coolcats.repository.UserRepository;
import com.coolcats.coolcats.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class UserController {

    private UserRepository userRepository;
    private UserService userService;

    @GetMapping("/register")
    public String showRegistration(Model model)
    {
        UserDto user = new UserDto();
        model.addAttribute("user",user);
        return "register";
    }

    @PostMapping("/register/submit")
    public String registerUser(@ModelAttribute("user") UserDto userDto, BindingResult result, Model model)
    {
        User existingUser = userRepository.findByUsername(userDto.getUsername());
        if(existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same nickname");
        }

        existingUser = userRepository.findByEmail(userDto.getEmail());
        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            System.out.printf(""+ existingUser.getJoinDate());
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/login")
    public String showLogon()
    {
        return "login";
    }

    @GetMapping("/profile")
    public String showProfile(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userRepository.findByUsername(currentUserName);

        model.addAttribute("user", user);

        return "profile";
    }

    @GetMapping("/profile/{id}")
    public String showProfileById(@PathVariable Long id, Model model)
    {
        User user = userRepository.findById(id).orElse(null);

        if(user == null)
        {
            return "redirect:/profile?error";
        }

        model.addAttribute("user", user);
        return "profile";
    }
}
