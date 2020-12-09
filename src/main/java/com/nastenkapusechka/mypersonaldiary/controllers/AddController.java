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

        if (secretRepository.existsById(secret.getId())) {

            Secret s = secretRepository.findById(secret.getId()).get();
            s.setTitle(secret.getTitle());
            s.setContent(secret.getContent());
            secretRepository.save(s);

            return "redirect:/show";
        }

        User user = userRepository.findByUsername(principal.getName()).get();
        secret.setUser(user);
        secretRepository.save(secret);
        log.info("Secret saved");
        return "redirect:/show";
    }
}
