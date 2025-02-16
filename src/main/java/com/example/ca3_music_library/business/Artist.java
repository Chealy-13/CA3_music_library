package com.example.ca3_music_library.business;
import lombok.*;
/**
 *
 * @author Sophie
 *
 */
@Getter
// Add setter methods
@Setter
// Add a toString method
@ToString
// Add equals and hashcode methods - only include the specifically noted variables
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// Add the ability to build object with any components in any order
@Builder
// Add an all-args constructor
@AllArgsConstructor
public class Artist {
//    CREATE TABLE artists
//            (
//                    artistId INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
//                    name     VARCHAR(30) NOT NULL
//);
// Fields
    private int artistID;
    private String artistName;

}
