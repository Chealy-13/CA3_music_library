package com.example.ca3_music_library.controllers;

import com.example.ca3_music_library.Persistence.AlbumDao;
import com.example.ca3_music_library.business.Album;
import com.example.ca3_music_library.business.Song;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AlbumController {

    private final AlbumDao albumDao;

    public AlbumController(AlbumDao albumDao) {
        this.albumDao = albumDao;
    }

    /**
     * displays all albums for a specific artist.
     * @param artistId the ID of the artist whose albums are to be displayed.
     * @param model the object to pass data to the view.
     * @return displaying the artist's albums.
     */
    @GetMapping("/albums/artist/{artistId}")
    public String viewAlbumsByArtist(@PathVariable("artistId") int artistId, Model model) {
        List<Album> albums = albumDao.getAlbumsByArtistId(artistId);
        model.addAttribute("albums", albums);
        return "albumsByArtist"; // View name
    }

    /**
     * displays details of a specific album, including its songs.
     * @param albumId the ID of the album to be displayed.
     * @param model the object to pass data to the view.
     * @return displaying album details.
     */
    @GetMapping("/album/{albumId}")
    public String viewAlbumDetails(@PathVariable("albumId") int albumId, Model model) {
        Album album = albumDao.getByAlbumId(albumId);
        List<Song> songs = albumDao.getSongsForAlbum(albumId);
        model.addAttribute("album", album);
        model.addAttribute("songs", songs);
        return "albumDetails"; // View name
    }

    /**
     * Displays all albums in the system.
     * @param model the object to pass data to the view.
     * @return displaying all albums.
     */
    @GetMapping("/albums")
    public String viewAllAlbums(Model model) {
        List<Album> allAlbums = albumDao.getAlbumsByArtistId(0); // Placeholder: Adjust as needed
        model.addAttribute("albums", allAlbums);
        return "allAlbums"; // View name
    }
}