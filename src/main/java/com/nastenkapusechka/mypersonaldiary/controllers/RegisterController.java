package com.nastenkapusechka.mypersonaldiary.controllers;

import com.nastenkapusechka.mypersonaldiary.entities.RegisterUser;
import com.nastenkapusechka.mypersonaldiary.entities.User;
import com.nastenkapusechka.mypersonaldiary.service.MailSender;
import com.nastenkapusechka.mypersonaldiary.service.impl.UserServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class RegisterController {

    private final UserServiceImpl userService;
    private MailSender mailSender;

    @Value("${activation.address}")
    private String address;

    @Autowired
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    public RegisterController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String returnRegisterPage(Model model) {
        model.addAttribute("user", new RegisterUser());
        log.info("Return register page");
        return "register";
    }

    @SneakyThrows
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Validated RegisterUser user, BindingResult bindingResult) {

        if (!user.getPassword().equals(user.getRepeatPassword())) {
            bindingResult.rejectValue("repeatPassword", "", "Passwords aren't equals!");
            log.info("Add error - passwords are different");
        }

        User optionalUser = userService.findByUsername(user.getEmail());

        if (optionalUser != null) {
            bindingResult.rejectValue("email", "", "User with this email is already exists!");
            log.info("Add error - this username already exists");
        }

        if (bindingResult.hasErrors()) {
            log.info("BindingResults are not empty. Return register page again");
            return "register";
        }

        User dbUser = new User();
        dbUser.setUsername(user.getEmail());
        dbUser.setPassword(user.getPassword());
        dbUser.setFirstName(user.getFirstName());
        dbUser.setLastName(user.getLastName());
        dbUser.setGender(user.getGender());

        User info = userService.registerUser(dbUser);
        log.info("User {} saved", info.getId());

        String msg = String.format(
                "Hello, %s %s!\n" +
                        "Please, follow next link to verify your account - %s",
                info.getFirstName(), info.getLastName(),
                address + info.getActivationCode()
        );
        mailSender.send(info.getUsername(), "Activation code in personal diary", msg);

        return "activation-info";
    }

    @GetMapping("/activate/{code}")
    public String activateUser(@PathVariable String code) {

        if (userService.activateUser(code)) {
            return "redirect:/login-activated";
        } else {
            return "redirect:/login-activated-error";
        }
    }
}
