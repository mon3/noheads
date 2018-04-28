package com.wpam.noheads;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by monikas on 07.04.18.
 */

public class Song {

//    String artistId;
    String songId;
    String songTitle;
    String language;
//    ArrayList<Pair<String,String>> songList;



    public Song(){
//    Important to be able to retrieve the object in DatabaseReference using Song.getClass
    }


    public Song(String songId, String songTitle, String language) {
        this.songId = songId;
        this.songTitle = songTitle;
        this.language = language;

    }

    public Song(String songTitle, String language) {
        this.language = language;
        this.songTitle = songTitle;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


//    ToDO: modify function!!!
    public Map<String, Object> toMap() {
//        Addition on new artist to the map of artists already existing in the database
        HashMap<String, Object> resultSongMap = new HashMap<>();
        resultSongMap.put("songId", songId);
        resultSongMap.put("songTitle", songTitle);
        resultSongMap.put("language", language);
        return resultSongMap;
    }

}
