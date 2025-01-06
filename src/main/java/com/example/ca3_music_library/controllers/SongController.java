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


    /**
     * Constructs a SongController with the specified SongDao.
     *
     * @param songDao instance used for song-related database operations
     */
    public SongController(SongDao songDao){
        this.songDao = songDao;
    }

    /**
     * Handles requests to the "/songs" endpoint.
     * Retrieves all songs from the database and adds them to the model for rendering in the "songs" view.
     *
     * @param model used to pass data to the view
     * @return the name of the Thymeleaf template "songs"
     */
    @GetMapping("/songs")
    public String processRequest(Model model){
        List<Song> songs = songDao.getAllSongs();
        model.addAttribute("songs", songs);

        return "songs";
    }

    /**
     * Handles requests to the "/songs/search" endpoint.
     * Searches for songs based on the provided query and adds the search results to the model for rendering.
     *
     * @param query the search query provided by the user
     * @param model used to pass data to the view
     * @return the name of the Thymeleaf template ("songSearchResults")
     */
    @GetMapping("/songs/search")
    public String searchSongs(@RequestParam("query") String query, Model model) {
        List<Song> searchResults = songDao.searchSongs(query);
        model.addAttribute("songSearchResults", searchResults);
        return "songSearchResults";
    }

    /**
     * Handles requests to the "/randomSong" endpoint.
     * Retrieves a random song from the database and adds it to the model for rendering in the "randomSong" view.
     *
     * @param model used to pass data to the view
     * @return the name of the Thymeleaf template ("randomSong")
     */
    @GetMapping("/randomSong")
    public String getRandomSong(Model model) {
        Song randomSong = songDao.getRandomSong();
        model.addAttribute("song", randomSong);
        return "randomSong";
    }

    /*
     * Feature: Random Song selector/generator
     * Developer: Sophie Nardone
     * Description: The Random Song Selector feature randomly selects a song from the music library database and displays it to the user
     *
     * - Created a method called getRandomSong to select a random song from the songs table
     */





}



