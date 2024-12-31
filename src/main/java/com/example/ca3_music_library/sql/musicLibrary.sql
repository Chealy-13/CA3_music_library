/**
 *
 * @author Damian/Chris
 *
 */
DROP DATABASE IF EXISTS musiclibary;
CREATE DATABASE IF NOT EXISTS musiclibary;

USE musiclibary;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    userId   INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)         NOT NULL,
    password VARCHAR(100)        NOT NULL,
    email    VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE artists
(
    artistId INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(30) NOT NULL
);

CREATE TABLE album
(
    albumId      INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    albumTitle        VARCHAR(50)  NOT NULL,
    artistId     INT UNSIGNED NOT NULL,
    releaseDate  DATE         NOT NULL,

    CONSTRAINT album_artist_fk FOREIGN KEY (artistId) REFERENCES artists(artistId)
);

CREATE TABLE songs
(
    songId         INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    songTitle          VARCHAR(50)  NOT NULL,
    albumId        INT UNSIGNED,
    artistId       INT UNSIGNED,
    additionalInfo VARCHAR(100),

    CONSTRAINT fk_song_album FOREIGN KEY (albumId) REFERENCES album(albumId),

    CONSTRAINT fk_song_artist FOREIGN KEY (artistId) REFERENCES artists(artistId)
);

CREATE TABLE playlist
(
    playlistId INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    userId     INT UNSIGNED NOT NULL,
    name       VARCHAR(50),
    type       INT(2),

    CONSTRAINT fk_playlist_user FOREIGN KEY (userId) REFERENCES users(userId)
);
-- inserts to populate the db
-- creating artists
INSERT INTO artists (name) VALUES ('Ed Sheeran');
INSERT INTO artists (name) VALUES ('The Beatles');
INSERT INTO artists (name) VALUES ('Taylor Swift');
INSERT INTO artists (name) VALUES ('Drake');
INSERT INTO artists (name) VALUES ('Coldplay');
INSERT INTO artists (name) VALUES ('The Weeknd');

-- creating albums
INSERT INTO album (albumTitle, artistId, releaseDate) VALUES ('+', 1, '2011-01-01');
INSERT INTO album (albumTitle, artistId, releaseDate) VALUES ('x', 1, '2014-01-01');
INSERT INTO album (albumTitle, artistId, releaseDate) VALUES ('%', 1, '2017-02-01');

INSERT INTO album (albumTitle, artistId, releaseDate) VALUES ('Abbey Road', 2, '1969-03-01');

INSERT INTO album (albumTitle, artistId, releaseDate) VALUES ('Red', 3, '2012-04-01');
INSERT INTO album (albumTitle, artistId, releaseDate) VALUES ('Midnights', 3, '2022-04-01');

INSERT INTO album (albumTitle, artistId, releaseDate) VALUES ('Take Care', 4, '2011-04-01');
INSERT INTO album (albumTitle, artistId, releaseDate) VALUES ('Views', 4, '2016-04-01');

INSERT INTO album (albumTitle, artistId, releaseDate) VALUES ('A Rush of Blood to the Head', 5, '2002-04-01');
INSERT INTO album (albumTitle, artistId, releaseDate) VALUES ('Parachutes', 5, '2000-04-01');

INSERT INTO album (albumTitle, artistId, releaseDate) VALUES ('Starboy', 6, '2016-04-01');
INSERT INTO album (albumTitle, artistId, releaseDate) VALUES ('After Hours', 6, '2020-04-01');

# --Songs from + Ed Sheeran
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('The A Team', 1, 1, '+');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Drunk', 1, 1, '+');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Lego House', 1, 1, '+');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('You Need Me, I Don’t Need You', 1, 1, '+');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Small Bump', 1, 1, '+');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Runaway', 1, 1, '+');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Wake Me Up', 1, 1, '+');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('The City', 1, 1, '+');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Gold Rush', 1, 1, '+');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Sunburn', 1, 1, '+');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('The A Team (Acoustic)', 1, 1, '+');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Give Me Love', 1, 1, '+');

# --Songs from x Ed Sheeran
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('One', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('I\'m a Mess', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Sing', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Don\'t', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Nina', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Thinking out Loud', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Photograph', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Bloodstream', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Tenerife Sea', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Runaway', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('The Man', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Afire Love', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Even My Dad Does Sometimes', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Shirtsleeves', 2, 1, 'x');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('I See Fire', 2, 1, 'x');

# --Songs from Abbey Road the beatles
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Come Together', 4, 2, 'Abbey Road');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Something', 4, 2, 'Abbey Road');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Here Comes The Sun', 4, 2, 'Abbey Road');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Golden Slumbers', 4, 2, 'Abbey Road');

# --Songs from Red Taylor swift
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Red', 5, 3, 'Red');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('I Knew You Were Trouble.', 5, 3, 'Red');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('22', 5, 3, 'Red');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('All Too Well', 5, 3, 'Red');

# --Songs from Midnights Taylor swift
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Anti-Hero', 6, 3, 'Midnights');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Karma', 6, 3, 'Midnights');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Midnight Rain', 6, 3, 'Midnights');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Lavender Haze', 6, 3, 'Midnights');

# --Songs from Take Care Drake
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Headlines', 7, 4, 'Take Care');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Take Care', 7, 4, 'Take Care');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('HYFR', 7, 4, 'Take Care');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('The Motto', 7, 4, 'Take Care');

# --Songs from Views Drake
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Controlla', 8, 4, 'Views');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('One Dance', 8, 4, 'Views');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Too Good', 8, 4, 'Views');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Hotline Bling', 8, 4, 'Views');


# --A Rush of Blood to the Head
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Clocks', 9, 5, 'A Rush of Blood to the Head');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('The Scientist', 9, 5, 'A Rush of Blood to the Head');

# --Songs from Parachutes Coldplay
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Sparks', 10, 5, 'Parachutes');
INSERT INTO songs (songTitle, albumId, artistId, additionalInfo) VALUES ('Yellow', 10, 5, 'Parachutes');


CREATE TABLE Rating (
    ratingId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    songId INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),

FOREIGN KEY (userId ) REFERENCES Users(userId )


);
