package com.nastenkapusechka.mypersonaldiary.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/login-error")
    public String errorModifyPage(Model model) {
        model.addAttribute("error", true);
        return "login";
    }

    @RequestMapping("/login-activated")
    public String activatedLoginPage(Model model) {
        model.addAttribute("activated", true);
        return "login";
    }

    @RequestMapping("/login-activated-error")
    public String activatedLoginPageError(Model model) {
        model.addAttribute("activated", false);
        return "login";
    }
}
