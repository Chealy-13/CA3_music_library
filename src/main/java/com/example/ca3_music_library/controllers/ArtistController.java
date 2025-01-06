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

    /**
     * Constructs ArtistController with the specified ArtistDao.
     *
     * @param artistDao  instance used for artist-related database operations
     */
    public ArtistController(ArtistDao artistDao){
        this.artistDao = artistDao;
    }

    /**
     * Handles requests to the "/artists" endpoint.
     * Retrieves all artists from the database and adds them to the model for rendering in the "artists" view.
     *
     * @param model used to pass data to the view
     * @return the name of the Thymeleaf template ("artists") to render
     */
    @GetMapping("/artists")
    public String viewArtists(Model model){
     List<Artist> artists = artistDao.getAllArtists();
     model.addAttribute("artists", artists);
     return "artists";
    }

    /**
     * Handles requests to the "/viewArtist/{id}" endpoint.
     * Retrieves the details of a specific artist and their songs from the database based on the artist ID.
     * Adds the artist details and their songs to the model for rendering in the "artistDetails" view.
     *
     * @param artistId the ID of the artist to retrieve
     * @param model used to pass data to the view
     * @return the name of the Thymeleaf template ("artistDetails")
     */
    @GetMapping("/viewArtist/{id}")
    public String viewArtist(@PathVariable("id") int artistId, Model model) {
        Artist artist = artistDao.getArtistByArtistId(artistId);
        List<Song> songsByArtist = artistDao.getSongsByArtist(artistId);
        model.addAttribute("artist", artist);
        model.addAttribute("songs", songsByArtist);
        return "artistDetails";
    }

}
