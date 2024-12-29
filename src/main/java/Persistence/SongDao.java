package Persistence;
/**
 *
 * @author Sophie
 *
 */
import business.Song;

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

}
