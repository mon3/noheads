package com.wpam.noheads;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monikas on 07.04.18.
 */

public class Artist {

    String artistId;
    String artistName;

    public Artist(){
//        Default constructor required for calls to DataSnapshot.getvValue(Artist.class);
    }

    public Artist(String artistId, String artistName) {
        this.artistId = artistId;
        this.artistName = artistName;
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

    public Map<String, Object> toMap() {
//        Addition on new artist to the map of artists already existing in the database
        HashMap<String, Object> resultArtistMap = new HashMap<>();
        resultArtistMap.put("artistId", artistId);
        resultArtistMap.put("artistName", artistName);
        return resultArtistMap;
    }

}
