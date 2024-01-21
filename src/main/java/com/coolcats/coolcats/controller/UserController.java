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
import org.springframework.web.bind.annotation.*;

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
            result.rejectValue("username", null,
                    "Konto o takiej nazwie użytkownika już istnieje!");
        }

        existingUser = userRepository.findByEmail(userDto.getEmail());
        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            System.out.printf(""+ existingUser.getJoinDate());
            result.rejectValue("email", null,
                    "Konto z takim adresem email już istnieje!");
        }
        String usernameRegex = "^[a-zA-Z0-9_-]{4,100}$";
        if (!userDto.getUsername().matches(usernameRegex)) {
            result.rejectValue("username", null, "Nieprawidłowa nazwa użytkonwika! (min. 4 znaki, maks. 100 znaków, dozwolone użycie _-)");
        }
        String emailRegex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!userDto.getEmail().matches(emailRegex)) {
            result.rejectValue("email", null, "Niepoprawny adres email!");
        }
        String passwdRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,255}$";
        if (!userDto.getPasswd().matches(passwdRegex)) {
            result.rejectValue("passwd", null, "Hasło musi zawierać przynajmniej jedną dużą i małą literę, jedną cyfrę i jeden znak specjalny! (min. 8 znaków)");
        }

        if(result.hasErrors()){
            model.addAttribute("error",result);
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String showLogon(@RequestParam(name = "success", required = false) String success, Model model)
    {
        if (success!=null) {
            model.addAttribute("successMessage", "Rejestracja zakończona sukcesem! Teraz możesz się zalogować!");
        }
        System.out.println(success);
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
