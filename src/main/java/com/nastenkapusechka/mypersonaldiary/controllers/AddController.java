package com.nastenkapusechka.mypersonaldiary.controllers;

import com.nastenkapusechka.mypersonaldiary.entities.Secret;
import com.nastenkapusechka.mypersonaldiary.entities.User;
import com.nastenkapusechka.mypersonaldiary.repo.SecretRepository;
import com.nastenkapusechka.mypersonaldiary.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;


@Controller
@Slf4j
public class AddController {

    private final SecretRepository secretRepository;
    private final UserServiceImpl userService;

    @Autowired
    public AddController(SecretRepository secretRepository, UserServiceImpl userService) {
        this.secretRepository = secretRepository;
        this.userService = userService;
    }

    @GetMapping("/add")
    public String getAddForm(Model model) {
        model.addAttribute("secret", new Secret());
        log.info("Return add form");
        return "add";
    }

    @PostMapping("/add")
    public String saveSecret(@ModelAttribute @Valid Secret secret,  BindingResult result, Principal principal) {

        if (result.hasErrors()) return "add";

        Optional<Secret> s = secretRepository.findById(secret.getId());

        if (s.isPresent()) {

            Secret secret1 = s.get();
            secret1.setTitle(secret.getTitle());
            secret1.setContent(secret.getContent());
            secretRepository.save(secret1);

            return "redirect:/show";
        }

        User user = userService.findByUsername(principal.getName());
        secret.setUser(user);
        secret.setDateOfCreating(LocalDate.now());
        secretRepository.save(secret);

        log.info("Secret saved");
        return "redirect:/show";
    }
}
