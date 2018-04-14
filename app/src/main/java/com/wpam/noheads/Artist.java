package com.wpam.noheads;

/**
 * Created by monikas on 07.04.18.
 */

public class Artist {

    String artistId;
    String artistName;

    public Artist(){

    }

    public Artist(String artistId, String artistName) {
        this.artistId = artistId;
        this.artistName = artistName;
//        TODO: add songs to Artist
    }


    public Artist(String artistId, String artistName, String songTitle, String language) {
        this.artistId = artistId;
        this.artistName = artistName;
        Song song = new Song(songTitle, language);
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
