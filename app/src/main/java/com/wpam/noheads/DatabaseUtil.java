package com.wpam.noheads;


import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by monikas on 14.04.18.
 */

public class DatabaseUtil{

    public static void findSongByArtistId(String artistId, String newSong, final Listener listener) {
        FirebaseDatabase.getInstance().getReference("songs").child(artistId).orderByChild("songTitle").equalTo(newSong).limitToFirst(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Song song = dataSnapshot.getValue(Song.class);
                Log.v("Song inside: ", song.getSongTitle());
                listener.onSongRetrieved(song);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    interface Listener{
        void onSongRetrieved(Song song);
    }
}
