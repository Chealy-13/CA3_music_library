package Persistence;

import java.util.List;

/**
 * Interface that specifies the standard operation to be performed on a generic obj.
 * example a Playlist obj,  song obj, user obj etc.
 * @param <T>: T is the obj to be specify in the implementation class
 */
public interface Dao <T>{
    /**
     * Method to retrieve obj.
     * @param id: Id of the obj to retrieve from db.
     * @return: Reurn the obj. if pfound in db else return null
     */
    T getObj(int id);

    /**
     * Method to retrieve all objects from a db.
     * @return: Returns a list of object if not empty
     */
    List<T> getAllObj();

    /**
     * Method to Save obj into a db.
     * @param t: The obj to save
     * @return: Returns an Integer, 1 if saved & null otherwise
     */
    int saveObl(T t);


    /**
     * Method to Insert obj into a db.
     * @param t: The obj to insert into db
     * @return: Returns an Integer, 1 if inserted & null otherwise
     */
    int insertObj(T t);


    /**
     * Method to Update obj in a db.
     * @param t: The obj to update
     * @return: Returns an Integer, 1 if saved & null otherwise
     */
    int updateObj(T t);


    /**
     * Method to Delete obj from a db.
     * @param t: The obj to delete
     * @return: Returns an Integer, 1 if deleted & null otherwise
     */
    int deleteObj(T t);


}
