package com.nastenkapusechka.mypersonaldiary.controllers;

import com.google.gson.Gson;
import com.nastenkapusechka.mypersonaldiary.entities.Compliment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class ComplimentController {

    @GetMapping("/compliment")
    public String getCompliment(Model model) throws Exception {

        HttpURLConnection connection = (HttpURLConnection) new URL("https://complimentr.com/api").openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.connect();

        Gson gson = new Gson();
        Compliment compliment = null;

        if (connection.getResponseCode() == 200) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                compliment = gson.fromJson(reader, Compliment.class);
                compliment.setFirstLetterToUpperCase();
            }
        }
        assert compliment != null;
        model.addAttribute("compliment", compliment.getCompliment());
        return "compliment";
    }
}
