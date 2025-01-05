package com.example.ca3_music_library.Persistence;
/**
 *
 * @author Sophie
 *
 */
import com.example.ca3_music_library.business.Artist;
import com.example.ca3_music_library.business.Song;

import java.util.List;

public interface ArtistDao {
    Artist getArtistByArtistId(int artistId);

    List<Artist> getAllArtists();

    public Artist getArtistByName(String artistName);

    public List<Song> getSongsByArtist(int artistId);
    public boolean updateArtist(Artist artist);
    public boolean deleteArtist(int artistId);
}
