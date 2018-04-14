package com.wpam.noheads;

/**
 * Created by monikas on 07.04.18.
 */

public class Song {

    String songId;
    String songTitle;
    String language;
//    ToDO: add artist FK; move language to separate db table

//    Important to be able to retrieve the values
    public Song(){

    }


    public Song(String songId, String songTitle, String language) {
        this.songId = songId;
        this.songTitle = songTitle;
        this.language = language;
    }

    public Song(String songId, String songTitle) {
        this.songId = songId;
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
}
