package com.nastenkapusechka.mypersonaldiary.controllers;

import com.nastenkapusechka.mypersonaldiary.entities.Like;
import com.nastenkapusechka.mypersonaldiary.entities.Secret;
import com.nastenkapusechka.mypersonaldiary.entities.User;
import com.nastenkapusechka.mypersonaldiary.repo.LikesRepository;
import com.nastenkapusechka.mypersonaldiary.repo.SecretRepository;
import com.nastenkapusechka.mypersonaldiary.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    private UserRepository userRepository;
    private SecretRepository secretRepository;
    private LikesRepository likesRepository;

    @Autowired
    public void setLikesRepository(LikesRepository likesRepository) {
        this.likesRepository = likesRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setSecretRepository(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("likes", likesRepository.count());
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {

        User currentUser = userRepository.findByUsername(principal.getName()).get();
        List<Secret> userSecrets = secretRepository.findByUserUsername(principal.getName());
        model.addAttribute("user",currentUser);
        model.addAttribute("secretsSize", userSecrets.size());
        return "home";
    }

    @GetMapping("/like")
    public String likeApp() {
        likesRepository.save(new Like());
        return "redirect:/";
    }
}
