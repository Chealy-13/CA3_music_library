package com.example.ca3_music_library.Persistence;
/**
 *
 * @author Sophie
 *
 */
import com.example.ca3_music_library.business.Artist;
import com.example.ca3_music_library.business.Song;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArtistDaoImpl extends MySQLDao implements ArtistDao {

    /**
     * Constructs a new instance of the ArtistDaoImpl class.
     * This constructor initializes the data access object with the specified properties
     * file name. It calls the superclass constructor to load the properties needed for
     * database connections, such as driver, URL, database name, username, and password.
     * @param propertiesFilename the name of the properties file containing database
     * connection details; it cannot be null or empty.
     * @throws IllegalArgumentException if the provided propertiesFilename is null or empty.
     */
//    public ArtistDaoImpl(String propertiesFilename) {
//        super(propertiesFilename);
//    }

    /**
     * Retrieves an Artist object by its artist ID.
     * It establishes a connection to the database, executes a query
     * to find the artist with the specified ID, and maps the result to an
     * Artist object. If no artist is found, this method returns null.
     * @param artistId the ID of the artist to retrieve.
     * @return an Artist object corresponding to the specified artist ID,
     * or null if no artist is found.
     * @throws SQLException if a database access error occurs or if there is an
     * issue executing the query or processing the result set.
     */
    @Override
    public Artist getArtistByArtistId(int artistId) {
        Artist artist = null;

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM artists where artistId = ?")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, artistId);
            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                if (rs.next()) {
                    artist = mapRow(rs);
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when executing SQL or processing results.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the connection using the superclass method
            super.freeConnection(conn);
        }
        return artist;
    }

    /**
     * Retrieves a list of all Artist objects from the database.
     * It establishes a connection to the database, executes a query
     * to select all artists, and maps the results to a list of Artist
     * objects. If there are no artists in the database, an empty list is returned.
     * @return a List of Artist objects, or an empty list if no artists are found.
     * @throws SQLException if a database access error occurs or if there is an
     * issue executing the query or processing the result set.
     */
    @Override
    public List<Artist> getAllArtists() {
        List<Artist> artists = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM artists")) {

            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                while (rs.next()) {
                    artists.add(mapRow(rs));
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when executing SQL or processing results.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the connection using the superclass method
            super.freeConnection(conn);
        }
        return artists;
    }

    /**
     * Retrieves an Artist object from the database based on the artist's name.
     * It establishes a connection to the database, executes a query to find the artist
     * with the specified name, and maps the result to an Artist object.
     * If no artist is found with the provided name, this method returns null.
     *
     * @param artistName the name of the artist to retrieve.
     * @return an Artist object corresponding to the specified artist name,
     *         or null if no artist is found with that name.
     * @throws SQLException if a database access error occurs or if there is an issue
     *         executing the query or processing the result set.
     */
    @Override
    public Artist getArtistByName(String artistName) {
        Artist artist = null;

        Connection conn = super.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM artists WHERE name = ?")) {
            ps.setString(1, artistName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    artist = mapRow(rs);
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when executing SQL or processing results.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            super.freeConnection(conn);
        }
        return artist;
    }

    /**
     * Retrieves a list of Song objects from the database associated with a specific artist.
     * It establishes a connection to the database, executes a query to find all songs
     * by the given artist ID, and maps the results to a list of Song objects.
     * If no songs are found for the given artist ID, an empty list is returned.
     *
     * @param artistId the ID of the artist whose songs to retrieve.
     * @return a list of Song objects associated with the specified artist ID,
     *         or an empty list if no songs are found for that artist.
     * @throws SQLException if a database access error occurs or if there is an issue
     *         executing the query or processing the result set.
     */
    @Override
    public List<Song> getSongsByArtist(int artistId) {
        List<Song> songs = new ArrayList<>();

        Connection conn = super.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM songs WHERE artistId = ?")) {
            ps.setInt(1, artistId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Song song = new Song(
                            rs.getInt("songId"),
                            rs.getString("songTitle"),
                            rs.getInt("artistId"),
                            rs.getInt("albumId"),
                            rs.getString("additionalInfo")
                    );
                    songs.add(song);
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when executing SQL or processing results.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            super.freeConnection(conn);
        }
        return songs;
    }



    /**
     * Updates the name of an existing artist in the database based on the artist's ID.
     * It establishes a connection to the database, executes an update query, and checks
     * whether the update was successful by verifying if any rows were affected.
     *
     * @param artist the Artist object containing the updated information (e.g., artist name).
     * @return true if the artist was successfully updated, otherwise false.
     * @throws SQLException if a database access error occurs or if there is an issue
     *         executing the update query.
     */
    @Override
    public boolean updateArtist(Artist artist) {
        boolean updated = false;

        Connection conn = super.getConnection();
        String query = "UPDATE artists SET name = ? WHERE artistId = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, artist.getArtistName());
            ps.setInt(2, artist.getArtistID());
            int rowsAffected = ps.executeUpdate();
            updated = rowsAffected > 0; // Check if any rows were updated
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when updating artist");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            super.freeConnection(conn);
        }
        return updated;
    }

    /**
     * Deletes an artist from the database based on the artist's ID.
     * It establishes a connection to the database, executes a delete query, and checks
     * whether the deletion was successful by verifying if any rows were affected.
     *
     * @param artistId the ID of the artist to be deleted.
     * @return true if the artist was successfully deleted, otherwise false.
     * @throws SQLException if a database access error occurs or if there is an issue
     *         executing the delete query.
     */
    @Override
    public boolean deleteArtist(int artistId) {
        boolean deleted = false;

        Connection conn = super.getConnection();
        String query = "DELETE FROM artists WHERE artistId = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, artistId);
            int rowsAffected = ps.executeUpdate();
            deleted = rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when deleting artist");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            super.freeConnection(conn);
        }
        return deleted;
    }




    /**
     * Maps a single row of the result set to an Artist object.
     * This method extracts the artist's ID and name from the provided
     * ResultSet and constructs a new Artist instance using the extracted values.
     * It is used for converting a row from a
     * database query result into a corresponding object.
     * @param rs the ResultSet containing the current row of data
     * to be mapped to an Artist object.
     * @return an Artist object populated with the data from the
     * result set.
     * @throws SQLException if there is an error accessing the data in the
     * ResultSet.
     */
    private Artist mapRow(ResultSet rs) throws SQLException {
        return Artist.builder()
                .artistID(rs.getInt("artistId"))
                .artistName(rs.getString("name"))
                .build();
    }
}
