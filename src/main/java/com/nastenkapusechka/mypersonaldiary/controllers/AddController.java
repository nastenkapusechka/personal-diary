package com.nastenkapusechka.mypersonaldiary.controllers;

import com.nastenkapusechka.mypersonaldiary.entities.Secret;
import com.nastenkapusechka.mypersonaldiary.entities.User;
import com.nastenkapusechka.mypersonaldiary.repo.SecretRepository;
import com.nastenkapusechka.mypersonaldiary.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;


@Controller
public class AddController {

    private SecretRepository secretRepository;
    private UserRepository userRepository;
    private final Logger log= LoggerFactory.getLogger(AddController.class);

    @Autowired
    public void setSecretRepository(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
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
            log.info("Saving secret: secret id={} is already present in db", s.get().getId());
            Secret mySecret = s.get();
            mySecret.setTitle(secret.getTitle());
            mySecret.setContent(secret.getContent());
            secretRepository.deleteById(mySecret.getId());
            log.info("Secret deleted by id");
            secretRepository.save(mySecret);
            log.info("Edited secret saved");

            return "redirect:/show";
        }

        User user = userRepository.findByUsername(principal.getName()).get();
        secret.setUser(user);
        secretRepository.save(secret);
        log.info("Secret saved");
        return "redirect:/show";
    }
}
