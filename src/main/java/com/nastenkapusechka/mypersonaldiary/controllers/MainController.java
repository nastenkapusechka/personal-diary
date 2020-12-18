package com.nastenkapusechka.mypersonaldiary.controllers;

import com.nastenkapusechka.mypersonaldiary.entities.Like;
import com.nastenkapusechka.mypersonaldiary.entities.Secret;
import com.nastenkapusechka.mypersonaldiary.entities.User;
import com.nastenkapusechka.mypersonaldiary.repo.LikesRepository;
import com.nastenkapusechka.mypersonaldiary.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    private final LikesRepository likesRepository;
    private final UserServiceImpl userService;

    @Autowired
    public MainController(LikesRepository likesRepository, UserServiceImpl userService) {
        this.likesRepository = likesRepository;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("likes", likesRepository.count());
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {

        User currentUser = userService.findByUsername(principal.getName());
        List<Secret> userSecrets = userService.getUsersSecrets(principal.getName());
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
