package com.example.ca3_music_library.Persistence;

import com.example.ca3_music_library.business.Playlist;
import com.example.ca3_music_library.business.Song;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.ca3_music_library.utils.utils.mapRowToPlaylist;
import static com.example.ca3_music_library.utils.utils.mapRowToSong;

/**
 * Implementation of the PlayListDAO interface
 * to manage PlayList.java records in database.
 */
@Component
public class PlaylistDaoImpl extends MySQLDao implements PlaylistDao {

//    private final Connection connection;

    /**
     * Constructs a PlayListDAOImpl with the specified database connection.
     *
     * @param conn is the Connection object to connect to the database.
     */
//    public PlaylistDaoImpl(Connection conn) {
//        super();
//        this.connection = conn;
//    }

    /**
     * creates a new playlist in the database.
     * @param playlist the Playlist object containing the details of the playlist to be created,
     * like its name, wether it is public or private, and the creator's ID.
     * @return true if the playlist was created and false otherwise, mainly because of sql exceptions.
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
    @Override
    public boolean createPlaylist(Playlist playlist) {
        String sql = "INSERT INTO playlists (playlistName, isPublic, creatorId) VALUES (?, ?, ?)";
        Connection conn = super.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, playlist.getPlaylistName());
            ps.setBoolean(2, playlist.isPublic());
            ps.setInt(3, playlist.getCreatorId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * updates an existing playlist in the database.
     * @param playlist the Playlist object containing the updated details of the playlist,
     * like its name, wether it is public or private, and the unique playlist ID to identify
     * which playlist to update.
     * @return true if the playlist was successfully updated and false otherwise, mainly because of sql exceptions.
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     *
     */
    @Override
    public boolean updatePlaylist(Playlist playlist) {
        String sql = "UPDATE playlists SET playlistName = ?, isPublic = ? WHERE playlistId = ?";
        Connection conn = super.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, playlist.getPlaylistName());
            ps.setBoolean(2, playlist.isPublic());
            ps.setInt(3, playlist.getPlaylistId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * deletes a playlist from the database using its unique playlist ID.
     * @param playlistId the unique id of the playlist to be deleted.
     * @return true if the playlist was successfully deleted from the database or false otherwise,
     * mainly because of an SQL exception or if no playlist with the specified ID exists.
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
    @Override
    public boolean deletePlaylist(int playlistId) {
        String sql = "DELETE FROM playlists WHERE playlistId = ?";
        Connection conn = super.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * retrieves a playlist from the database based on its unique playlist ID.
     * @param playlistId the unique id of the playlist to get.
     * @return a Playlist object representing the playlist with the specified ID,
     * or null if no playlist with the given ID exists or an error occurs.
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     *
     * This method executes a SQL query to find a playlist by its ID. If a match is found, the result
     * set is mapped to a Playlist object using the mapRow method. If no match is
     * found, null is returned.
     */
    @Override
    public Playlist getPlaylistById(int playlistId) {
        String sql = "SELECT * FROM playlists WHERE playlistId = ?";
        Connection conn = super.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToPlaylist(rs);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();

        }
        return null;
    }

    /**
     * retrieves all playlists visible to the specified user. This includes:
     * - Public playlists (i.e. playlists where isPublic is TRUE)
     * - Private playlists created by the specified user (i.e. playlists where the creatorId matches the userId)
     * @param userId the unique id of the user requesting the playlists.
     * @return a List of Playlist objects visible to the user.
     * If no playlists are found or an error occurs, an empty list is returned.
     *
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     *
     * This method executes a SQL query to fetch playlists visible to the user. The mapRow method is used
     * to convert each row in the result set into a Playlist object, which is added to the result list.
     */
    @Override
    public List<Playlist> getAllPlaylists(int userId) {
        String sql = "SELECT * FROM playlists WHERE isPublic = TRUE OR creatorId = ?";
        List<Playlist> playlists = new ArrayList<>();
        Connection conn = super.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                playlists.add(mapRowToPlaylist(rs));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();

        }
        return playlists;
    }


    /**
     * retrieves a list of all public playlists from the database.
     * this method fetches playlists that are marked as public (when public flag is set to TRUE)
     * and maps each playlist record from the database into a Playlist object. If any SQL error
     * occurs during execution, it will be caught, logged, and an empty list will be returned.
     *
     * @return a List of Playlist objects representing all public playlists.
     * If no public playlists exist or an error occurs, an empty list is returned.
     */
    @Override
    public List<Playlist> getPublicPlaylists() {
        String sql = "SELECT * FROM playlists WHERE isPublic = TRUE";
        List<Playlist> playlists = new ArrayList<>();
        try (Connection conn = super.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                playlists.add(mapRowToPlaylist(rs));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();

        }
        return playlists;
    }

    /**
     * retrieves a list of playlists created by a specific user.
     * this method fetches all playlists from the database where the creator's ID matches the
     * specified user ID. Each playlist record is mapped into a Playlist object. If any
     * SQL error occurs during execution, it will be caught, logged, and an empty list will be returned
     *
     * @param userId the ID of the user whose playlists are to be retrieved.
     * @return a List of Playlist objects representing the playlists created by the user.
     * if no playlists exist for the user or an error occurs, an empty list is returned.
     */
    @Override
    public List<Playlist> getUserPlaylists(int userId) {
        String sql = "SELECT * FROM playlists WHERE creatorId = ?";
        List<Playlist> playlists = new ArrayList<>();
        try (Connection conn = super.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                playlists.add(mapRowToPlaylist(rs));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();

        }
        return playlists;
    }

    /**
     * Adds a song to a specific playlist in the database.
     * This method inserts a new entry into the playlist_songs table, which links a playlist
     * (checking its playlistId) with a song (checking its songId). This process makes sure
     * that the song is linked to the playlist for later retrieval or playback.
     * @param playlistId the unique id of the playlist to which the song will be added.
     * @param songId the unique id of the song to be added to the playlist.
     * @return true if the song was successfully added to the playlist, false otherwise.
     * @throws SQLException if a database access error occurs or other SQL execution issues.
     */
    @Override
    public boolean addSongToPlaylist(int playlistId, int songId) {
        String sql = "INSERT INTO playlist_songs (playlistId, songId) VALUES (?, ?)";
        try (Connection conn = super.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes a song from a specific playlist in the database.
     * This method deletes the link between a playlist and a song in the playlist_songs table,
     * removing the song from the playlist.
     * @param playlistId the unique id of the playlist from which the song will be removed.
     * @param songId the unique id of the song to be removed from the playlist.
     * @return true if the song was successfully removed from the playlist, false otherwise.
     * @throws SQLException if a database access error occurs or other SQL execution issues.
     */
    @Override
    public boolean removeSongFromPlaylist(int playlistId, int songId) {
        String sql = "DELETE FROM playlist_songs WHERE playlistId = ? AND songId = ?";
        try (Connection conn = super.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * gets all songs associated with a specific playlist from the database.
     * This method performs a query to join the songs and playlist_songs tables,
     * fetching all songs that belong to the specified playlist. The results are mapped
     * to Song objects and returned as a list.
     * @param playlistId the unique id of the playlist whose songs are to be retrieved.
     * @return a list of Song objects representing the songs in the specified playlist.
     * Returns an empty list if no songs are found or if an error occurs.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<Song> getSongsForPlaylist(int playlistId) {
        String sql = "SELECT s.* FROM songs s " +
                "JOIN playlist_songs ps ON s.songId = ps.songId " +
                "WHERE ps.playlistId = ?";
        List<Song> songs = new ArrayList<>();
        try (Connection conn = super.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                songs.add(mapRowToSong(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }

//    /**
//     * Maps a row from a ResultSet to a Playlist object.
//     * It extracts data from the provided ResultSet and
//     * constructs a new playlist instance using the extracted values.
//     * @param rs the ResultSet containing the current row of data
//     * to be mapped to a playlist object.
//     * @return a Playlist object populated with the values from the current
//     * row of the ResultSet.
//     * @throws SQLException if a database access error occurs or if there is an
//     * issue retrieving data from the ResultSet.
//     */
//    private Playlist mapRow(ResultSet rs) throws SQLException {
//        return Playlist.builder()
//                .playlistId(rs.getInt("playlistId"))
//                .playlistName(rs.getString("playlistName"))
//                .isPublic(rs.getBoolean("isPublic"))
//                .creatorId(rs.getInt("creatorId"))
//                .build();
//    }

}
