package com.nastenkapusechka.mypersonaldiary.controllers;

import com.nastenkapusechka.mypersonaldiary.entities.Secret;
import com.nastenkapusechka.mypersonaldiary.repo.SecretRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
public class ShowController {

    private final SecretRepository repository;

    @Autowired
    public ShowController(SecretRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/show")
    public String getSecrets(Model model, Principal principal) {
        List<Secret> secrets = repository.findByUserUsername(principal.getName());
        model.addAttribute("secrets", secrets);
        log.info("Return page with secrets (owner = user with username {})", principal.getName());
        return "list";
    }

    @GetMapping("/show/details/{id}")
    public String getDetails(@PathVariable Long id, Model model) {
        model.addAttribute("details", repository.findById(id).get());
        log.info("Return details about secret #{}", id);
        return "secret-details";
    }

    @GetMapping("/show/delete/{id}")
    public String deleteSecret(@PathVariable Long id) {
        log.info("Delete secret #{}", id);
        repository.deleteById(id);
        return "redirect:/show";
    }

    @GetMapping("/show/edit/{id}")
    public String editSecret(@PathVariable Long id, Model model) {
        log.info("Edit secret #{}", id);
        model.addAttribute("secret", repository.findById(id).get());
        return "add";
    }
}
