package controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class IndexController {
    @GetMapping("/")
    public String home() { return "index";}

    @GetMapping("/user_index")
    public String userIndex() { return "user_index";}

    @GetMapping("/Song_index")
    public String songIndex(){ return "Song_index"; }

    @GetMapping("/PlayList_index")
    public String playlistIndex(){ return "PlayList_index"; }

    @GetMapping("/artist_index")
    public String artistIndex(){ return "artist_index"; }

    @GetMapping("/album_index")
    public String albumIndex(){ return "album_index"; }

    @GetMapping("/registerPage")
    public String registration(){
        return "registration";
    }
}
