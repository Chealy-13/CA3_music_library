package Persistence;
/**
 *
 * @author Chris
 *
 */
import business.Song;

import java.util.List;

public interface SongDao {
    Song getSongBySongId(int songId);

    List<Song> getSongsBySongTitle(String songTitle);

    List<Song> getAllSongs();

//    Song getSongByArtistId(int artistId);

    List<Song> getSongsByArtistId(int artistId);

    List<Song> getSongsByAlbumId(int albumId);

    boolean deleteBySongId(int songId);

    boolean addSong(Song song);

}
