package com.wpam.noheads;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    Button buttonAdd;

    DatabaseReference databaseArtists;
    DatabaseReference databaseSongs;

    Context mContext;


    Map<String, String> artistsMap = new HashMap<>();
    Map<String, ArrayList<String>> songsMap = new HashMap<>(); // key = artistID, songsMap = list of songs


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        buttonAdd = findViewById(R.id.buttonAddDb);

        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
        databaseSongs = FirebaseDatabase.getInstance().getReference("songs");


        databaseArtists.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Artist artist = dataSnapshot.getValue(Artist.class);
                String artistName = artist.getArtistName();
                String artistId = artist.getArtistId();

                artistsMap.put(artistName, artistId);
                Log.v("ARTISTS DB", "onChildAdded: " + artistName);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Artist artist = dataSnapshot.getValue(Artist.class);
                String removedArtist = artist.getArtistName();
                Log.v("ARTISTS DB", "onChildRemoved: " + removedArtist);
                artistsMap.remove(removedArtist);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseSongs.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String songArtistId = dataSnapshot.getKey();
                Log.e("SONGS DB", "Artist's songs number: " + dataSnapshot.getChildrenCount());

                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    Song song = songSnapshot.getValue(Song.class);
                    String songTitle = song.getSongTitle();
                    Log.d("SONGS DB", songTitle);

                    if (songsMap.containsKey(songArtistId)) {
                        ArrayList<String> songsList = songsMap.get(songArtistId);
                        songsList.add(songTitle);
                    } else {
                        ArrayList<String> songs = new ArrayList<>();
                        songs.add(songTitle);
                        songsMap.put(songArtistId, songs);
                        Log.v("SONGS DB", "New song: " + songTitle);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String songArtistId = dataSnapshot.getKey();
                Log.e("SONGS DB", "Song list removed" + songArtistId);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addSongIntent = new Intent(mContext, AddSongActivity.class);

                Log.v("BUTTON: ", "button clicked");
                startActivity(addSongIntent);

            }
        });

    }


 }

