package Persistence;

import business.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Chris
 *
 */
public class SongDaoImpl extends MySQLDao implements SongDao {

    /**
     * Constructs a new instance of the SongDaoImpl class.
     * This constructor initializes the data access object with the specified properties
     * file name. It calls the superclass constructor to load the properties needed for
     * database connections, such as driver, URL, database name, username, and password.
     * @param propertiesFilename the name of the properties file containing database
     * connection details; it cannot be null or empty.
     * @throws IllegalArgumentException if the provided propertiesFilename is null or empty.
     */
//    public SongDaoImpl(String propertiesFilename) {
//
//        super(propertiesFilename);
//    }

//    public static void main(String[] args) {
//        CustomerDao customerDao = new CustomerDaoImpl("database.properties");
//        List<Customer> customers = customerDao.getAllCustomers();
//        System.out.println(customers);
//        System.out.println("------------------------------");
//        System.out.println("Customer with id 119: " + customerDao.getById(119));
//    }

    /**
     * Retrieves a song from the database using the specified song ID.
     * It establishes a connection to the database, prepares a SQL
     * statement to find the song with the given ID, and executes the query.
     * If a song with the specified ID is found, it is mapped to a Song
     * object and returned. If no song is found, the method returns null.
     * @param songId the ID of the song to retrieve.
     * @return the Song object representing the song with the specified ID,
     * or null if no such song exists.
     */
    @Override
    public Song getSongBySongId(int songId) {
        Song song = null;

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM songs where songId = ?")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, songId);
                    // TRY to execute the query
                    try (ResultSet rs = ps.executeQuery()) {
                        // Extract the information from the result set
                        // Use extraction method to avoid code repetition!
                        if(rs.next()) {
                            song = mapRow(rs);
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
        return song;
    }

    /**
     * Retrieves a list of songs from the database that match a specified title
     * it establishes a connection to the database, prepares a SQL query to find songs with
     * titles that match and executes the query. It does a case-insensitive
     * search by using the LOWER function on both the input and the song title in the database.
     * All matching songs are mapped to Song objects and returned in a list.
     * @param songTitle the title of the songs or song to search for.
     * @return a List of Song objects that match the specified title or an
     * empty list if no matches are found.
     */
    @Override
    public List<Song> getSongsBySongTitle(String songTitle) {
        List<Song> songs = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM songs WHERE LOWER(songTitle) LIKE LOWER(?)")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setString(1, "%" + songTitle + "%");
            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                while(rs.next()) {
                    songs.add(mapRow(rs));
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
        return songs;
    }

    /**
     * Gets a list of all songs from the database.
     * it establishes a database connection, executes a SQL query to retrieve all rows from
     * the "songs" table, and maps each row to a Song object. Each Song object is then
     * added to a list, which is returned after all records have been processed. If any SQL exceptions occur
     * during execution, they are logged for debugging.
     * @return a List of Song objects representing all songs in the database, or an empty
     * list if there are no songs available or if an error occurs.
     */
    @Override
    public List<Song> getAllSongs() {
        // Create variable to hold the customer info from the database
        List<Song> songs = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // Get a statement from the connection
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM songs")) {
            // Execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Repeatedly try to get a customer from the resultset
                while (rs.next()) {
                    Song s = mapRow(rs);
                    songs.add(s);
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

        return songs;
    }

    /**
     * gets a list of songs by a specific artist from the database based on the provided artist ID.
     * it establishes a database connection, prepares a SQL query to fetch all songs associated
     * with a specific artist ID, and maps each result to a Song object. The method adds each
     * Song object to a list, which is returned after all records have been processed.
     * SQL exceptions encountered during execution are logged for debugging.
     * @param artistId the unique id of the artist whose songs are to be retrieved.
     * @return a List of Song objects that are associated with the specified artist ID, or
     * an empty list if no songs are found or if an error occurs.
     */
    @Override
    public List<Song> getSongsByArtistId(int artistId) {
        List<Song> songs = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM songs WHERE artistId = ?")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, artistId);
            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                while(rs.next()) {
                    songs.add(mapRow(rs));
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
        return songs;
    }

    /**
     * gets a list of songs by a specific album from the database based on the provided album ID.
     * it establishes a database connection, prepares a SQL query to select all songs
     * associated with a given album ID, and maps each result to a Song object using the
     * mapRow method. Each Song object is added to a list, which is returned after
     * all records have been processed.
     * SQL exceptions encountered during query preparation or execution are logged for debugging.
     * @param albumId the unique id of the album whose songs are to be retrieved.
     * @return a List of Song objects associated with the specified album ID, or an
     * empty list if no songs are found or an error occurs.
     */
    @Override
    public List<Song> getSongsByAlbumId(int albumId) {
        List<Song> songs = new ArrayList<>();

        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM songs WHERE albumId = ?")) {

            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, albumId);
            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                while(rs.next()) {
                    songs.add(mapRow(rs));
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
        return songs;
    }

    /**
     * Deletes a song from the database by its unique song ID.
     * it establishes a database connection and prepares a SQL `DELETE` statement, which is
     * parameterized to target a specific song ID. It then executes the delete operation, and based
     * on the number of rows affected, returns `true` if the deletion was successful
     * or `false` if no rows were affected.
     * SQL exceptions encountered during query preparation or execution are logged for debugging.
     * @param songId the unique id of the song to be deleted.
     * @return true if the song was successfully deleted, false if no song with the
     * specified ID exists or an error occurs.
     */
    @Override
    public boolean deleteBySongId(int songId) {
        int rowsAffected = 0;

        Connection conn = super.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM songs where songId = ?")) {
            // Fill in the blanks, i.e. parameterize the query
            ps.setInt(1, songId);

            // Execute the operation
            // Remember that when you are doing an update, a delete or an insert,
            // your only result will be a number indicating how many rows were affected
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred when attempting to prepare/execute SQL.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the superclass method
            super.freeConnection(conn);
        }

        if (rowsAffected > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds a new song to the database.
     * This method inserts a song record into the `songs` table using the provided Song object, which
     * contains details like song title, album ID, artist ID, and additional information. After executing the
     * SQL `INSERT` operation, it attempts to retrieve the generated unique song ID and updates the `Song`
     * object with this ID if the insertion is successful.
     * SQL exceptions encountered during query preparation or execution are logged for debugging.
     * @param song the Song object containing song details to be added to the database.
     * @return true if the song was successfully added, false otherwise.
     */
    @Override
    public boolean addSong(Song song) {
        int rowsAffected = 0;

        // Get a connection using the superclass
        Connection conn = super.getConnection();

        String sql = "INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, song.getSongTitle());
            ps.setInt(2, song.getAlbumID());
            ps.setInt(3, song.getArtistID());
            ps.setString(4, song.getInfo());

            rowsAffected = ps.executeUpdate();

            // If we want to capture the generated song ID, we can retrieve it here
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        song.setSongID(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred while adding the song.");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the superclass method
            super.freeConnection(conn);
        }
        return rowsAffected > 0;
    }

    /**
     * Maps the current row of the given ResultSet to a Song object.
     * This method extracts the values of each relevant column from the current row of the
     * ResultSet and uses them to build and return a new Song instance. The
     * fields include the song ID, song title, album ID, artist ID, and any additional information.
     * @param rs the ResultSet positioned at the current row to map.
     * @return a new Song object populated with data from the current row of the ResultSet.
     * @throws SQLException if a database access error occurs or if the column labels are incorrect.
     */
    private Song mapRow(ResultSet rs) throws SQLException {
        // Get the pieces of a customer from the resultset and create a new Customer
        Song s = Song.builder()
                .songID(rs.getInt("songId"))
                .songTitle(rs.getString("songTitle"))
                .albumID(rs.getInt("albumId"))
                .artistID(rs.getInt("artistId"))
                .info(rs.getString("additionalInfo"))
                .build();
        // Return the extracted Customer (or null if the resultset was empty)
        return s;
    }
}