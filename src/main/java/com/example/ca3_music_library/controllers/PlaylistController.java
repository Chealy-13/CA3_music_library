package com.example.ca3_music_library.controllers;

import com.example.ca3_music_library.Persistence.PlaylistDao;
import com.example.ca3_music_library.Persistence.SongDao;
import com.example.ca3_music_library.business.Playlist;
import com.example.ca3_music_library.business.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistDao playlistDao;
    private final SongDao songDao;


    /**
     * Creates a new PlaylistController instance with the provided DAOs.
     * @param playlistDao the data access object for playlist-related operations.
     * @param songDao the data access object for song-related operations.
     */
    @Autowired
    public PlaylistController(PlaylistDao playlistDao, SongDao songDao) {
        this.playlistDao = playlistDao;
        this.songDao = songDao;
    }

    /**
     * Displays all playlists accessible to a user,
     * including public playlists and their own playlists.
     * @param userId the ID of the user for whom to display playlists.
     * @param model the Model object to pass data to the view.
     * @return displaying the list of playlists.
     */
    @GetMapping
    public String viewAllPlaylists(@RequestParam("userId") int userId, Model model) {
        List<Playlist> userPlaylists = playlistDao.getUserPlaylists(userId);
        List<Playlist> publicPlaylists = playlistDao.getPublicPlaylists();
        model.addAttribute("userPlaylists", userPlaylists);
        model.addAttribute("publicPlaylists", publicPlaylists);
        return "playlists";
    }

    /**
     * Displays details of a specific playlist, including the songs in the playlist.
     * @param playlistId the ID of the playlist to display.
     * @param model the Model object to pass data to the view.
     * @return displaying the playlist details.
     */
    @GetMapping("/{id}")
    public String viewPlaylistDetails(@PathVariable("id") int playlistId, Model model) {
        Playlist playlist = playlistDao.getPlaylistById(playlistId);
        List<Song> songs = playlistDao.getSongsForPlaylist(playlistId);
        model.addAttribute("playlist", playlist);
        model.addAttribute("songs", songs);
        return "playlistDetails";
    }

    /**
     * Creates a new playlist.
     * @param name the name of the playlist to create.
     * @param isPublic whether the playlist is public or private.
     * @param creatorId the ID of the user creating the playlist.
     * @return a redirect to the list of playlists.
     */
//    @PostMapping("/create")
//    public String createPlaylist(@RequestParam String name, @RequestParam boolean isPublic, @RequestParam int creatorId) {
//        Playlist newPlaylist = Playlist.builder()
//                .playlistName(name)
//                .isPublic(isPublic)
//                .creatorId(creatorId)
//                .build();
//        playlistDao.createPlaylist(newPlaylist);
//        return "redirect:/playlists";
//    }
    @PostMapping("/create")
    public String createPlaylist(@RequestParam String name, @RequestParam boolean isPublic, @RequestParam int creatorId, Model model) {
        System.out.println("Playlist Name: " + name);
        System.out.println("Is Public: " + isPublic);
        System.out.println("Creator ID: " + creatorId);

        try {
            Playlist newPlaylist = Playlist.builder()
                    .playlistName(name)
                    .isPublic(isPublic)
                    .creatorId(creatorId)
                    .build();

            boolean success = playlistDao.createPlaylist(newPlaylist);
            if (!success) {
                model.addAttribute("error", "Failed to create playlist.");
                return "error";
            }
            return "redirect:/playlists?userId=" + creatorId;
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
            return "error";
        }
    }


    /**
     * Adds a song to a playlist.
     * @param playlistId the ID of the playlist to which the song should be added.
     * @param songId the ID of the song to add to the playlist.
     * @return a redirect to the playlist details page.
     */
    @PostMapping("/{id}/addSong")
    public String addSongToPlaylist(@PathVariable("id") int playlistId, @RequestParam("songId") int songId) {
        playlistDao.addSongToPlaylist(playlistId, songId);
        return "redirect:/playlists/" + playlistId;
    }

    /**
     * Removes a song from a playlist.
     * @param playlistId the ID of the playlist from which the song should be removed.
     * @param songId the ID of the song to remove from the playlist.
     * @return a redirect to the playlist details page.
     */
    @PostMapping("/{id}/removeSong")
    public String removeSongFromPlaylist(@PathVariable("id") int playlistId, @RequestParam("songId") int songId) {
        playlistDao.removeSongFromPlaylist(playlistId, songId);
        return "redirect:/playlists/" + playlistId;
    }

    /**
     * Deletes a playlist.
     * @param playlistId the ID of the playlist to delete.
     * @return a redirect to the list of playlists.
     */
    @PostMapping("/{id}/delete")
    public String deletePlaylist(@PathVariable("id") int playlistId) {
        playlistDao.deletePlaylist(playlistId);
        return "redirect:/playlists";
    }

    @PostMapping("/{id}/rename")
    public String renamePlaylist(@PathVariable("id") int playlistId, @RequestParam("name") String name) {
        Playlist playlist = playlistDao.getPlaylistById(playlistId);
        if (playlist != null) {
            playlist.setPlaylistName(name);
            playlistDao.updatePlaylist(playlist);
        }
        return "redirect:/playlists/" + playlistId;
    }
}