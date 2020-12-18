package com.nastenkapusechka.mypersonaldiary.controllers;

import com.nastenkapusechka.mypersonaldiary.entities.User;
import com.nastenkapusechka.mypersonaldiary.service.impl.UserServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class RegisterController {

    //private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;

    @Autowired
    public RegisterController(UserServiceImpl userService) {
        this.userService = userService;
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

        User optionalUser = userService.findByUsername(user.getUsername());
        if (optionalUser != null) {
            bindingResult.rejectValue("username", "", "User with this username is already exists!");
            log.info("Add error - this username already exists");
        }

        if (bindingResult.hasErrors()) {
            log.info("BindingResults are not empty. Return register page again");
            return "register";
        }

        User info = userService.registerUser(user);
        log.info("User {} saved", info.getId());

        return "redirect:/login";
    }
}
