package Persistence;

import business.Rating;

import java.sql.SQLException;
import java.util.List;

public interface RatingDAO {

    boolean rateSong(int songId, int userId, int rating)throws SQLException;
    List<Rating> getRatingsByUser(int userId) throws SQLException;

}
