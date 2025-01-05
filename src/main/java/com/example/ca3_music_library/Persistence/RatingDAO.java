package com.example.ca3_music_library.Persistence;

import com.example.ca3_music_library.business.Rating;
import com.example.ca3_music_library.business.Song;

import java.sql.SQLException;
import java.util.List;

public interface RatingDAO {

    boolean rateSong(int songId, int userId, int rating)throws SQLException;
    List<Rating> getRatingsByUser(int userId) throws SQLException;

    Song getTopRatedSong();

    Song getMostPopularSong();
}
