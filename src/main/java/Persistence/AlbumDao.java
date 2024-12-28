package Persistence;
/**
 *
 * @author Chris
 *
 */
import business.Album;
import business.Song;

import java.sql.SQLException;
import java.util.List;

public interface AlbumDao {
    public Album getByAlbumId(int albumId);

    List<Album> getAlbumsByArtistId(int artistId);

    List<Song> getSongsForAlbum(int albumId);
}
