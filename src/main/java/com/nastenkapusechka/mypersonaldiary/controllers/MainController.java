package com.nastenkapusechka.mypersonaldiary.controllers;

import com.nastenkapusechka.mypersonaldiary.entities.Image;
import com.nastenkapusechka.mypersonaldiary.entities.Like;
import com.nastenkapusechka.mypersonaldiary.entities.Secret;
import com.nastenkapusechka.mypersonaldiary.entities.User;
import com.nastenkapusechka.mypersonaldiary.repo.ImageRepository;
import com.nastenkapusechka.mypersonaldiary.repo.LikesRepository;
import com.nastenkapusechka.mypersonaldiary.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
public class MainController {

    private final LikesRepository likesRepository;
    private final UserServiceImpl userService;
    private ImageRepository imageRepository;

    @Value("${image.url}")
    private String imageUrl;

    @Autowired
    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

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

        Image image = imageRepository.findByUserUsername(principal.getName());
        imageUrl = image == null ? imageUrl : image.getName();
        log.info("Image name: {}", image.getName());

        model.addAttribute("image", imageUrl);
        model.addAttribute("user", currentUser);
        model.addAttribute("secretsSize", userSecrets.size());

        //TODO fix that image not displayed

        return "home";
    }

    @GetMapping("/like")
    public String likeApp() {
        likesRepository.save(new Like());
        return "redirect:/";
    }
}
