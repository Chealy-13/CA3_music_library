package com.example.ca3_music_library.Persistence;

import com.example.ca3_music_library.business.Rating;
import com.example.ca3_music_library.business.Song;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.ca3_music_library.utils.utils.mapRowToSong;

@Component
public class RatingDaoImpl implements RatingDao {
    private Connection connection;

    public RatingDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * Rates a song and inserts into the Ratings table.
     * This method takes the user's rating for a song, ratings from range from 1 to 5.
     * @param songId The ID of the song being rated.
     * @param userId The ID of the user giving the rating.
     * @param rating The rating value.
     * @return true if the rating was successfully added to the database, false otherwise.
     * @throws SQLException if a database access error occurs or the SQL problem.
     */
    @Override
    public boolean rateSong(int userId, int songId, int rating) throws SQLException {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        String sql = "INSERT INTO Ratings (userId, songId, rating) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.setInt(3, rating);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Gets a list of ratings given by a specific user.
     * This method executes a SQL query to collect all ratings associated with the
     * user. It takes a list of Rating
     * objects, each representing a rating associated with a song.
     *
     * @param userId the ID of the user whose ratings are to be collected
     * @return a list of Rating objects containing the song IDs, user IDs, and ratings
     * for the specified user. Returns an empty list if non are found.
     * @throws SQLException if a database access error occurs or the SQL statement is wrong
     */
    public List<Rating> getRatingsByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM ratings WHERE userId = ?";

        List<Rating> ratings = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ratings.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return ratings;
    }

    @Override
    public Song getTopRatedSong() {
        String sql = "SELECT s.*, AVG(r.rating) as avgRating FROM songs s " +
                "JOIN ratings r ON s.songId = r.songId " +
                "GROUP BY s.songId ORDER BY avgRating DESC LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToSong(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Song getMostPopularSong() {
        String sql = "SELECT s.*, COUNT(ps.songId) as playlistCount FROM songs s " +
                "JOIN playlist_songs ps ON s.songId = ps.songId " +
                "GROUP BY s.songId ORDER BY playlistCount DESC LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToSong(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Rating mapRow(ResultSet rs) throws SQLException {
        return Rating.builder()
                .ratingId(rs.getInt("ratingId"))
                .userId(rs.getInt("userId"))
                .songId(rs.getInt("songId"))
                .rating(rs.getInt("rating"))
                .build();
    }
}