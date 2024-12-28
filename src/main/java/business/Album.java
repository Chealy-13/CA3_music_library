package business;
/**
 *
 * @author Chris
 *
 */
import java.util.Date;
import java.util.List;

import lombok.*;
@Getter
// Add setter methds
@Setter
// Add a toString method
@ToString
// Add equals and hashcode methods - only include the specifically noted variables
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// Add the ability to build object with any components in any order
@Builder
// Add an all-args constructor
@AllArgsConstructor
public class Album {
//    CREATE TABLE album
//            (
//                    albumId      INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
//                    albumTitle        VARCHAR(50)  NOT NULL,
//    artistId     INT UNSIGNED NOT NULL,
//    releaseDate  DATE         NOT NULL,
//    ratingOfSongs INT UNSIGNED NOT NULL,
//
//    CONSTRAINT album_artist_fk FOREIGN KEY (artistId) REFERENCES artists(artistId)
//            );

    @EqualsAndHashCode.Include
    private int albumId;
    @NonNull
    private String albumTitle;
    private int artistId;
    private Date releaseDate;

    private List<Song> songs;
}
