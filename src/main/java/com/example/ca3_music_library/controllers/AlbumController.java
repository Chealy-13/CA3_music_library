package com.example.ca3_music_library.controllers;

import com.example.ca3_music_library.Persistence.AlbumDao;
import com.example.ca3_music_library.business.Album;
import com.example.ca3_music_library.business.Song;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class AlbumController {
    private final AlbumDao albumDao;

    public AlbumController(AlbumDao albumDao){
        this.albumDao = albumDao;
    }

//    @GetMapping("/viewAlbums")
//    public String viewAlbums(Model model){
//        List<Album> albums = albumDao.getAlbumsByArtistId();
//        model.addAttribute("albums", albums);
//        return "albums";
//    }

    @GetMapping("/viewAlbum/{id}")
    public String viewAlbum(@PathVariable("id") int albumId, Model model) {
        Album album = albumDao.getByAlbumId(albumId);
        List<Song> getSongsForAlbum = albumDao.getSongsForAlbum(albumId);
        model.addAttribute("album", album);
        model.addAttribute("songs", getSongsForAlbum);
        return "albumDetails";
    }
}
