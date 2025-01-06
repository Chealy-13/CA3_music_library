package com.example.ca3_music_library.Persistence;
/**
 *
 * @author Sophie
 *
 */
import com.example.ca3_music_library.business.Song;

import java.util.List;

public interface SongDao {
    Song getSongBySongId(int songId);

    List<Song> getSongsBySongTitle(String songTitle);

    List<Song> getAllSongs();

    List<Song> getSongsByArtistId(int artistId);

    List<Song> getSongsByAlbumId(int albumId);

    boolean deleteBySongId(int songId);

    boolean addSong(Song song);

    List<Song> getSongsRatedByUser(int userId);

    boolean rateSong(int songId, int userId, int rating);

    Song getTopRatedSong();

    Song getMostPopularSong();

    List<Song> searchSongs(String query);

    Song getRandomSong();

    List<Song> getSongsForAlbum(int albumId);

    /*
     * Feature: Random Song selector/generator
     * Developer: Sophie Nardone
     * Description: The Random Song Selector feature randomly selects a song from the music library database and displays it to the user
     *
     * - Created a method called getRandomSong to select a random song from the songs table
     */



}
