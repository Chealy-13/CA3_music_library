package com.example.ca3_music_library.Persistence;
/**
 *
 * @author Julie
 *
 */
import com.example.ca3_music_library.business.Playlist;
import com.example.ca3_music_library.business.Song;

import java.util.List;

public interface PlaylistDao {
    boolean createPlaylist(Playlist playlist);

    boolean updatePlaylist(Playlist playlist);

    boolean deletePlaylist(int playlistId);

    Playlist getPlaylistById(int playlistId);

    List<Playlist> getAllPlaylists(int userId);

    List<Playlist> getPublicPlaylists();

    List<Playlist> getUserPlaylists(int userId);

    boolean addSongToPlaylist(int playlistId, int songId);

    boolean removeSongFromPlaylist(int playlistId, int songId);

    List<Song> getSongsForPlaylist(int playlistId);
}
