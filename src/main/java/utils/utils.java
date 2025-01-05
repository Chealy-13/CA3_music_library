package utils;

import business.Song;
import business.Playlist;

import java.sql.ResultSet;
import java.sql.SQLException;

public class utils {

    /**
     * Maps a row from the ResultSet to a Song object.
     *
     * @param rs the ResultSet positioned at the current row
     * @return a Song object populated with data from the current row
     * @throws SQLException if there is an issue accessing the ResultSet
     */
    public static Song mapRowToSong(ResultSet rs) throws SQLException {
        return Song.builder()
                .songID(rs.getInt("songId"))
                .songTitle(rs.getString("songTitle"))
                .albumID(rs.getInt("albumId"))
                .artistID(rs.getInt("artistId"))
                .additionalInfo(rs.getString("additionalInfo"))
                .build();
    }

    /**
     * Maps a row from the ResultSet to a Playlist object.
     *
     * @param rs the ResultSet positioned at the current row
     * @return a Playlist object populated with data from the current row
     * @throws SQLException if there is an issue accessing the ResultSet
     */
    public static Playlist mapRowToPlaylist(ResultSet rs) throws SQLException {
        return Playlist.builder()
                .playlistId(rs.getInt("playlistId"))
                .playlistName(rs.getString("playlistName"))
                .isPublic(rs.getBoolean("isPublic"))
                .creatorId(rs.getInt("creatorId"))
                .build();
    }
}
