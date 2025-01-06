package com.example.ca3_music_library.controllers;

import com.example.ca3_music_library.Persistence.AlbumDao;
import com.example.ca3_music_library.Persistence.SongDao;
import com.example.ca3_music_library.business.Album;
import com.example.ca3_music_library.business.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
//@RequestMapping("/albums")
public class AlbumController {

    private final AlbumDao albumDao;
    private final SongDao songDao;

    @Autowired
    public AlbumController(AlbumDao albumDao, SongDao songDao) {
        this.albumDao = albumDao;
        this.songDao = songDao;
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
    @GetMapping("/albums/{id}")
    public String viewAlbumDetails(@PathVariable("id") int albumId, Model model) {
        Album album = albumDao.getByAlbumId(albumId);
        if (album == null) {
            model.addAttribute("error", "Album not found");
            return "error";
        }
        List<Song> songs = songDao.getSongsForAlbum(albumId);
        model.addAttribute("album", album);
        model.addAttribute("songs", songs);
        return "albumDetails";
    }

    /**
     * Displays all albums in the system.
     * @param model the object to pass data to the view.
     * @return displaying all albums.
     */
    @GetMapping("/albums")
    public String viewAllAlbums(Model model) {
        List<Album> allAlbums = albumDao.getAllAlbums();
        model.addAttribute("albums", allAlbums);
        return "allAlbums"; // View name
    }
}