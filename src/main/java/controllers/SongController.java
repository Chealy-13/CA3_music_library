package controllers;

import Persistence.SongDao;
import business.Song;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class SongController {

    private final SongDao songDao;

    public SongController(SongDao songDao){
        this.songDao = songDao;
    }

    @GetMapping("/viewSongs")
    public String processRequest(Model model){
        List<Song> songs = songDao.getAllSongs();
        model.addAttribute("songs", songs);

        return "songs";
    }






    }



