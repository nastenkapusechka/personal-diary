package com.nastenkapusechka.mypersonaldiary.controllers;

import com.nastenkapusechka.mypersonaldiary.entities.User;
import com.nastenkapusechka.mypersonaldiary.repo.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@Slf4j
public class RegisterController {

    //private final AuthenticationManager authenticationManager;
    private final   UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String returnRegisterPage(Model model) {
        model.addAttribute("user", new User());
        log.info("Return register page");
        return "register";
    }

    @SneakyThrows
    @PostMapping("/register")
    public String registerUser(@ModelAttribute @Validated User user, BindingResult bindingResult, HttpServletRequest request) {

        if (!user.getPassword().equals(user.getRepeatPassword())) {
            bindingResult.rejectValue("repeatPassword", "", "Passwords aren't equals!");
            log.info("Add error - passwords are different");
        }
        Optional<User> optionalUser = repository.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            bindingResult.rejectValue("username", "", "User with this username is already exists!");
            log.info("Add error - this username already exists");
        }

        if (bindingResult.hasErrors()) {
            log.info("BindingResults are not empty. Return register page again");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setFirstName(user.getFirstName().trim());
        user.setLastName(user.getLastName().trim());
        user.setUsername(user.getUsername().trim());
        user.setRegistrationDate(LocalDate.now());
        User info = repository.save(user);
        log.info("User {} saved", info.getId());


        return "redirect:/login";
    }
}
