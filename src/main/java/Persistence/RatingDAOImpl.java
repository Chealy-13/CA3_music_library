package Persistence;

import business.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingDAOImpl implements RatingDAO {
    private Connection connection;

    public RatingDAOImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * Rates a song and inserts into the Ratings table.
     * This method takes the user's rating for a song, ratings from range from 1 to 5.
     * @param songId_ The ID of the song being rated.
     * @param userId_ The ID of the user giving the rating.
     * @param rating_ The rating value.
     * @return true if the rating was successfully added to the database, false otherwise.
     * @throws SQLException if a database access error occurs or the SQL problem.
     */
    @Override
    public boolean rateSong(int songId_, int userId_, int rating_) throws SQLException {
        String sql = "INSERT INTO Ratings (songId, userId, rating) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, songId_);
            ps.setInt(2, userId_);
            ps.setInt(3, rating_);
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
     * @param userId_ the ID of the user whose ratings are to be collected
     * @return a list of Rating objects containing the song IDs, user IDs, and ratings
     * for the specified user. Returns an empty list if non are found.
     * @throws SQLException if a database access error occurs or the SQL statement is wrong
     */
    public List<RatingDAO> getRatingsByUser(int userId_) throws SQLException {
        String sql = "SELECT songId, userId, rating FROM Rating WHERE userId = ?";

        List<Rating> ratings = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId_);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ratings.add(new Rating(
                            rs.getInt("songID"),
                            rs.getInt("userID"),
                            rs.getInt("rating")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}