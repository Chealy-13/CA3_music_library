package com.example.ca3_music_library.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String getHome(HttpSession session, Model model) {
         String successMessage = (String) session.getAttribute("successMessage");

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }

        return "index";
    }




    @GetMapping("/Song_index")
    public String songIndex() {
        return "Song_index";
    }

    @GetMapping("/PlayList_index")
    public String playlistIndex() {
        return "PlayList_index";
    }

    @GetMapping("/artist_index")
    public String artistIndex() {
        return "artist_index";
    }

    @GetMapping("/album_index")
    public String albumIndex() {
        return "album_index";
    }

    @GetMapping("/registerPage")
    public String registration() {
        return "registration";
    }
    @GetMapping("/loginPage")
    public String showLoginPage() {
        return "login";

    }}