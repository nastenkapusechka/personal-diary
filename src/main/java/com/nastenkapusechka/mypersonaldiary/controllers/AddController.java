package com.nastenkapusechka.mypersonaldiary.controllers;

import com.nastenkapusechka.mypersonaldiary.entities.Secret;
import com.nastenkapusechka.mypersonaldiary.repo.SecretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
public class AddController {

    private final SecretRepository repository;

    @Autowired
    public AddController(SecretRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/add")
    public String getAddForm(Model model) {
        model.addAttribute("secret", new Secret());
        return "add";
    }

    @PostMapping("/add")
    public String saveSecret(@ModelAttribute @Valid Secret secret, BindingResult result) {

        if (result.hasErrors()) return "add";

        repository.save(secret);

        return "redirect:/";
    }
}
