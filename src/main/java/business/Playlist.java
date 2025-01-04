package business;
/**
 *
 * @author Chris
 *
 */
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
public class Playlist {
    // CREATE TABLE playlists
    // (
    //     playlistId      INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    //     playlistName    VARCHAR(50)  NOT NULL,
    //     isPublic        BOOLEAN      NOT NULL,
    //     creatorId       INT UNSIGNED NOT NULL,
    //
    //     CONSTRAINT playlist_creator_fk FOREIGN KEY (creatorId) REFERENCES users(userId)
    // );

    @EqualsAndHashCode.Include
    private int playlistId;
    @NonNull
    private String playlistName;
    private boolean isPublic;
    private int creatorId;

    private List<Song> songs;
}