package com.nastenkapusechka.mypersonaldiary.controllers;

import com.nastenkapusechka.mypersonaldiary.repo.SecretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/show/details/{id}")
    public String getDetails(@PathVariable Long id, Model model) {
        model.addAttribute("details", repository.findById(id).get());
        return "secret-details";
    }

    @GetMapping("/show/delete/{id}")
    public String deleteSecret(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/show";
    }
}
