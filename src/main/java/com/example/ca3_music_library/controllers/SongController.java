package com.example.ca3_music_library.controllers;

import com.example.ca3_music_library.Persistence.SongDao;
import com.example.ca3_music_library.business.Song;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class SongController {

    private final SongDao songDao;


    public SongController(SongDao songDao){
        this.songDao = songDao;
    }


    @GetMapping("/songs")
    public String processRequest(Model model){
        List<Song> songs = songDao.getAllSongs();
        model.addAttribute("songs", songs);

        return "songs";
    }

    @GetMapping("/songs/search")
    public String searchSongs(@RequestParam("query") String query, Model model) {
        List<Song> searchResults = songDao.searchSongs(query);
        model.addAttribute("songSearchResults", searchResults);
        return "songSearchResults";
    }







}



