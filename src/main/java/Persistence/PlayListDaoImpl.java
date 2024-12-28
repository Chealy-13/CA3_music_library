package Persistence;

import business.Artist;
import business.PlayList;
import business.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the PlayListDAO interface
 * to manage PlayList records in database.
 */
public  class PlayListDaoImpl extends MySQLDao implements PlayListDao{

    private final Connection connection;

    /**
     * Constructs a PlayListDAOImpl with the specified database connection.
     * @param conn is the Connection object to connect to the database.
     */
    public PlayListDaoImpl(Connection conn) {
        super();
        this.connection = conn;
    }


    /**
     * Method to retrieve a playlist from db by it id.
     * @param id: Id of row/obj/playlist to return from db.
     * @return: Return an obj/playlist.
     */
    @Override
    public PlayList getPlayListById(int id) {
        String sql = "SELECT * FROM playlist WHERE playlistId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PlayList(
                            rs.getInt("playlistId"),
                            rs.getInt("userId"),
                            rs.getString("name"),
                            rs.getInt("type")

                    );
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();

            }
        } catch (SQLException e) {
            System.out.println("ClassNotFoundException occurred when trying to load driver: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to delete a playlist from a database
     * @param id: The id of the playlist to be deleted from db
     * @return: A boolean, True if rowAffect/deteted OR False if nothing was deleted
     */
    @Override
    public boolean deletePlayListById(int id) {
        int rowsAffected = 0;
        String sqlQuery = "DELETE  FROM playlist WHERE playlistId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery))
        {
            ps.setInt(1, id);
            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
                System.out.println("SQL Exception occurred when attempting to prepare SQL for execution.");
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();

        } finally {
            // Close the connection
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }

        }

        if (rowsAffected > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addSongToPlayList(Song s) {
        return false;
    }

    @Override
    public boolean deleteSongFromPlatList(int songId) {
        return false;
    }

    @Override
    public List<Song> getAllSongsOnPlayList(){
        List<Song> favouriteSongs = new ArrayList<>();
        // Get a connection using the superclass
        Connection conn = super.getConnection();
        // TRY to get a statement from the connection
        // When you are parameterizing the query, remember that you need
        // to use the ? notation (so you can fill in the blanks later)
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM playlist")) {

            // TRY to execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Extract the information from the result set
                // Use extraction method to avoid code repetition!
                if (rs.next()) {
                    favouriteSongs.add(mapRow(rs));
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
        return favouriteSongs;

    }
    private Song mapRow(ResultSet rs) throws SQLException {
        return Song.builder()
                .songID(rs.getInt("songId"))
                .songTitle(rs.getString("title"))
                .albumID(rs.getInt("albumId"))
                .artistID(rs.getInt("artsistId"))

                .build();
    }
}
