package com.example.ca3_music_library.Persistence;
/**
 *
 * @author Chris
 *
 */

import com.example.ca3_music_library.business.Album;
import com.example.ca3_music_library.business.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Component
public class AlbumDaoImpl extends MySQLDao implements AlbumDao {
    private final SongDao songDao;
    @Autowired
    public AlbumDaoImpl(SongDao songDao) {
        this.songDao = songDao;
    }
    /**
     * Constructs a new instance of the AlbumDaoImpl class.
     * This constructor initializes the data access object with the specified properties
     * file name. It calls the superclass constructor to load the properties needed for
     * database connections, such as driver, URL, database name, username, and password.
     * @param propertiesFilename the name of the properties file containing database
     * connection details; it cannot be null or empty.
     * @throws IllegalArgumentException if the provided propertiesFilename is null or empty.
     */
//    public AlbumDaoImpl(String propertiesFilename) {
//        super(propertiesFilename);
//    }


    /**
     * Retrieves an Album object by its unique album ID, including the songs belonging to the album.
     * it uses a parameterized SQL query to find an album record in the database
     * matching the specified album ID. It extracts the album information and any related
     * songs for the album, returning a fully populated Album object.
     * @param albumId the unique id of the album to retrieve.
     * @return an Album object populated with the album's details and associated songs,
     * or null if no album is found for the given ID.
     * @throws SQLException if a database access error occurs or the SQL execution fails.
     */
    @Override
    public Album getByAlbumId(int albumId) {
        Album album = null;

        String sql = "SELECT a.*, ar.name AS artistName " +
                "FROM album a " +
                "JOIN artists ar ON a.artistId = ar.artistId " +
                "WHERE a.albumId = ?";
        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, albumId);
            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                if (rs.next()) {
                    album = mapRow(rs);
                    List<Song> songs = songDao.getSongsForAlbum(albumId);
                    album.setSongs(songs);
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
        return album;
    }

    /**
     * Retrieves a list of Album objects associated with a specific artist ID.
     * it uses a parameterized SQL query to locate all album records
     * linked to the provided artist ID. For each album retrieved, it populates
     * the album's song list by retrieving songs that belong to that album.
     * @param artistId the unique id of the artist whose albums are to be retrieved.
     * @return a List of Album objects with each album's details and associated songs.
     * Returns an empty list if the artist has no albums or the ID does not exist.
     */
    @Override
    public List<Album> getAlbumsByArtistId(int artistId) {
        List<Album> albums = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM album where artistId = ?")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, artistId);
            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                while (rs.next()) {
                    Album album = mapRow(rs);
                    album.setSongs(songDao.getSongsForAlbum(album.getAlbumId()));
                    albums.add(album);
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
        return albums;
    }



    /**
     * Retrieves a list of all albums from the database.
     * It executes a SQL query to fetch all album records and maps each record
     * to an Album object. If an album has associated songs, they are also retrieved.
     * @return a List of Album objects containing all albums in the database.
     * Returns an empty list if no albums are found or if an error occurs.
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
    @Override
    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();
        Connection conn = super.getConnection();

        String sql = "SELECT a.*, ar.name AS artistName " +
                "FROM album a " +
                "JOIN artists ar ON a.artistId = ar.artistId";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Album album = Album.builder()
                        .albumId(rs.getInt("albumId"))
                        .albumTitle(rs.getString("albumTitle"))
                        .artistId(rs.getInt("artistId"))
                        .releaseDate(rs.getDate("releaseDate"))
                        .artistName(rs.getString("artistName"))
                        .build();

                album.setSongs(songDao.getSongsForAlbum(album.getAlbumId()));
                albums.add(album);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred when retrieving all albums.");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            super.freeConnection(conn);
        }

        return albums;
    }
    /**
     * Maps a row from a ResultSet to an Album object.
     * It extracts data from the provided ResultSet and
     * constructs a new Album instance using the extracted values.
     * @param rs the ResultSet containing the current row of data
     * to be mapped to an Album object.
     * @return an Album object populated with the values from the current
     * row of the ResultSet.
     * @throws SQLException if a database access error occurs or if there is an
     * issue retrieving data from the ResultSet.
     */
    private Album mapRow(ResultSet rs) throws SQLException {
        return Album.builder()
                .albumId(rs.getInt("albumId"))
                .albumTitle(rs.getString("albumTitle"))
                .artistId(rs.getInt("artistId"))
                .artistName(rs.getString("artistName"))
                .releaseDate(rs.getDate("releaseDate"))
                .build();
    }
}
