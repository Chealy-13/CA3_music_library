package Persistence;
/**
 *
 * @author Julie
 *
 */
import business.PlayList;
import business.Song;

import java.util.List;

public interface PlayListDao {
    public PlayList getPlayListById(int id);
    public boolean deletePlayListById(int id);
    public  boolean addSongToPlayList(Song s);
    public  boolean deleteSongFromPlatList(int songId);
    public List<Song> getAllSongsOnPlayList();
}
