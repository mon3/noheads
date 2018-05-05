package com.wpam.noheads.Model;

import com.wpam.noheads.Model.Song;

import java.util.ArrayList;

/**
 * Created by monikas on 28.04.18.
 */

public class SongList {

        String artistId;
        ArrayList<Song> songList;

        public SongList(){
            //    Important to be able to retrieve the object in DatabaseReference using Song.getClass
        }


        public SongList(String artistId) {
            this.artistId = artistId;
            this.songList = new ArrayList<>();
        }

        public SongList(String artistId, String songTitle, String language) {
            this.artistId = artistId;
            this.songList.add(new Song(songTitle, language));
        }

        public SongList(String artistId, Song song) {
        this.artistId = artistId;
        this.songList.add(song);
        }


        public String getArtistId () {
            return artistId;
        }

        public void setArtistId(String artistId) {
            this.artistId = artistId;
        }

        public ArrayList<Song> getSongList(){
            return songList;
        }

        public void setSongList(ArrayList songList) {
            this.songList = songList;
        }

}
