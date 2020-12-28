package com.nastenkapusechka.mypersonaldiary.controllers;

import com.nastenkapusechka.mypersonaldiary.entities.Image;
import com.nastenkapusechka.mypersonaldiary.entities.User;
import com.nastenkapusechka.mypersonaldiary.repo.ImageRepository;
import com.nastenkapusechka.mypersonaldiary.service.impl.UserServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.UUID;

@Controller
@Slf4j
public class SettingsController {

    private UserServiceImpl userService;
    private PasswordEncoder encoder;
    private ImageRepository imageRepository;

    @Value("${upload.path}")
    private String path;

    @GetMapping("/settings")
    public String getSettingsPage(Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "settings";
    }


    @PostMapping("/settings/change_name")
    public String changeName(@ModelAttribute User user, Model model) {

        model.addAttribute("user", user);

        if (user.getFirstName().trim().equals("") || user.getLastName().trim().equals("")) {
            model.addAttribute("user", user);
            model.addAttribute("emptyString", "First name or last name is empty!");
            return "settings";
        }
        user.setActivated(true);
        userService.saveUser(user);
        model.addAttribute("successName", "Success!");

        return "settings";
    }

    @PostMapping("/settings/change-password")
    public String changePassword(Model model, Principal principal,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String repeatPassword) {

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        if (!encoder.matches(oldPassword, user.getPassword())) {
            model.addAttribute("oldPasswordErr", "Old password is invalid!");
            return "settings";
        } else if (!newPassword.equals(repeatPassword) || newPassword.trim().equals("") || newPassword.trim().length() < 8) {
            model.addAttribute("repeatPasswordErr", "New passwords aren't equals, shorter 8 symbols or empty!");
            return "settings";
        }

        model.addAttribute("success", "Success!");
        user.setPassword(encoder.encode(newPassword));
        userService.saveUser(user);

        return "settings";
    }

    @SneakyThrows
    @PostMapping("/settings/load-avatar")
    public String loadAvatar(@RequestParam("file") MultipartFile multipartFile, Principal principal,
                             Model model) {

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        log.info("Filename: {}", multipartFile == null ? null : multipartFile.getOriginalFilename());

        if (multipartFile != null) {
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "_" + multipartFile.getOriginalFilename();
            multipartFile.transferTo(new File(path + fileName));

            Image image = imageRepository.findByUserUsername(principal.getName());
            if (image == null) image = new Image();

            image.setName(path + fileName);
            image.setUser(user);
            user.setImage(image);
            imageRepository.save(image);

            log.info("Filename: {}", fileName);
            model.addAttribute("successFile", "Success!");

        } else {
            model.addAttribute("uploadErr", "File is invalid or null");
        }
        return "settings";
    }

    @Autowired
    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }
}
