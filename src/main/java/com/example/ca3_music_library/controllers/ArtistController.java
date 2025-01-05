package com.example.ca3_music_library.controllers;

import com.example.ca3_music_library.Persistence.ArtistDao;
import com.example.ca3_music_library.business.Artist;
import com.example.ca3_music_library.business.Song;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ArtistController {

    private final ArtistDao artistDao;

    public ArtistController(ArtistDao artistDao){
        this.artistDao = artistDao;
    }

    @GetMapping("/viewArtists")
    public String viewArtists(Model model){
     List<Artist> artists = artistDao.getAllArtists();
     model.addAttribute("artists", artists);
     return "artists";
    }

    @GetMapping("/viewArtist/{id}")
    public String viewArtist(@PathVariable("id") int artistId, Model model) {
        Artist artist = artistDao.getArtistByArtistId(artistId);
        List<Song> songsByArtist = artistDao.getSongsByArtist(artistId);
        model.addAttribute("artist", artist);
        model.addAttribute("songs", songsByArtist);
        return "artistDetails";
    }

}
