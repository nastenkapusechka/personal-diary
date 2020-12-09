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
}
