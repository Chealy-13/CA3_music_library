package com.example.ca3_music_library.controllers;

import com.example.ca3_music_library.Persistence.RatingDao;
import com.example.ca3_music_library.business.Rating;
import com.example.ca3_music_library.business.Song;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/ratings")
public class RatingController {

    private final RatingDao ratingDao;

    /**
     * Creates a new RatingController instance with the provided RatingDao.
     * @param ratingDao the data access object for rating-related operations.
     */
    public RatingController(RatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    /**
     * Rates a song by a specific user.
     * This method accepts the user ID, song ID, and rating value,
     * and updates the ratings table in the database.
     * @param userId the ID of the user rating the song.
     * @param songId the ID of the song being rated.
     * @param rating the rating value (between 1 and 5).
     * @return a redirect to the user's ratings page.
     * @throws SQLException if a database access error occurs.
     */
    @PostMapping("/rateSong")
    public String rateSong(@RequestParam("userId") int userId, @RequestParam("songId") int songId, @RequestParam("rating") int rating) throws SQLException {
        ratingDao.rateSong(userId, songId, rating);
        return "redirect:/ratings/user/" + userId;
    }

    /**
     * Gets all ratings given by a specific user.
     * Fetches the list of songs rated by the user and their corresponding ratings.
     * @param userId the ID of the user whose ratings are to be retrieved.
     * @param model the Model object to pass data to the view.
     * @return the name of the view displaying the user's ratings.
     * @throws SQLException if a database access error occurs.
     */
    @GetMapping("/user/{userId}")
    public String getUserRatings(@PathVariable("userId") int userId, Model model) throws SQLException {
        List<Rating> ratings = ratingDao.getRatingsByUser(userId);
        model.addAttribute("ratings", ratings);
        return "userRatings";
    }

    /**
     * Retrieves the top-rated song in the system based on average ratings.
     * @param model the Model object to pass data to the view.
     * @return displays the top-rated song.
     */
    @GetMapping("/topRatedSong")
    public String getTopRatedSong(Model model) {
        Song topRatedSong = ratingDao.getTopRatedSong();
        model.addAttribute("topRatedSong", topRatedSong);
        return "topRatedSong";
    }

    /**
     * Gets the most popular song in the system based on
     * the number of playlists it appears in.
     * @param model the Model object to pass data to the view.
     * @return displays the most popular song.
     */
    @GetMapping("/mostPopularSong")
    public String getMostPopularSong(Model model) {
        Song mostPopularSong = ratingDao.getMostPopularSong();
        model.addAttribute("mostPopularSong", mostPopularSong);
        return "mostPopularSong";
    }
}
