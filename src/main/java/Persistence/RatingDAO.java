package Persistence;
import business.Rating;

import java.sql.SQLException;
import java.util.List;

public interface RatingDAO {

    boolean rateSong(int songId_, int userId_, int rating_)throws SQLException;
    List<RatingDAO> getRatingsByUser(int userId) throws SQLException;

}
