package com.nastenkapusechka.mypersonaldiary.controllers;

import com.nastenkapusechka.mypersonaldiary.repo.SecretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShowController {

    private final SecretRepository repository;

    @Autowired
    public ShowController(SecretRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/show")
    public String getSecrets(Model model) {
        model.addAttribute("secrets", repository.findAll());
        return "list";
    }
}
