package com.example.ca3_music_library.Persistence;
/**
 *
 * @author Chris
 *
 */
import com.example.ca3_music_library.business.Album;
import com.example.ca3_music_library.business.Song;

import java.util.List;

public interface AlbumDao {
    public Album getByAlbumId(int albumId);

    List<Album> getAlbumsByArtistId(int artistId);

//    List<Song> getSongsForAlbum(int albumId);

    List<Album> getAllAlbums();
}
