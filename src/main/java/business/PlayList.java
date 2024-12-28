package business;
/**
 *
 * @author Julie
 *
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayList {
    /*
    CREATE TABLE Playlist
(
    playlistId INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    userId     INT UNSIGNED NOT NULL,
    name       VARCHAR(50),
    type       INT(2),

    CONSTRAINT fk_playlist_user FOREIGN KEY (userId) REFERENCES users(userId)
);
    */
    // Fields
    private int playlistId;
    private int userId;
    private String name;
    private int type;



}
