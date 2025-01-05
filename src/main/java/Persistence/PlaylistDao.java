package Persistence;
/**
 *
 * @author Julie
 *
 */
import business.Playlist;
import business.Song;

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
