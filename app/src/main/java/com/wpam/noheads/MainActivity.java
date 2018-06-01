package com.wpam.noheads;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wpam.noheads.Model.Artist;
import com.wpam.noheads.Model.Song;
import com.wpam.noheads.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";

    Button buttonAdd;
    Button loginStart;

    DatabaseReference databaseArtists;
    DatabaseReference databaseSongs;

    Context mContext;


    Map<String, String> artistsMap = new HashMap<>(); // key = artist ID, value = Artist Name
    Map<String, ArrayList<String>> songsMap = new HashMap<>(); // key = artistID, songsMap = list of songs

//    private ActivityMainBinding binding;
    private String withId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        buttonAdd = findViewById(R.id.buttonNewDb);
        loginStart = findViewById(R.id.buttonLoginStart);

//        fetchArtists();
//        Log.d(LOG_TAG, "number of artist_songs: " + songsMap.size());
//        for (Map.Entry<String, ArrayList<String>> entry: songsMap.entrySet())
//        {
//            Log.e(LOG_TAG, " artist data: " + entry.getKey());
//
//            for (String song: entry.getValue()) {
//                Log.e(LOG_TAG, "song: " + song);
//
//            }
//
//        }
//        Log.d(LOG_TAG, "Random song: " + songsMap.get("Kombi"));

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent addSongIntent = new Intent(mContext, AddSongActivity.class);
                Intent addSongIntent = new Intent(mContext, AddSongActivity.class);
//                addSongIntent.putExtra("artistsData", (Serializable) artistsMap);
//                addSongIntent.putExtra("songsData", (Serializable) songsMap);
                Log.v("BUTTON: ", "button add song clicked");
                startActivity(addSongIntent);

            }
        });

        loginStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(mContext, LoginActivity.class);
//                addSongIntent.putExtra("artistsData", (Serializable) artistsMap);
//                addSongIntent.putExtra("songsData", (Serializable) songsMap);
                Log.v("BUTTON: ", "button login  clicked");
                startActivity(loginIntent);
            }
        });

    }




    private void fetchArtists() {
        FirebaseDatabase.getInstance().getReference().child("artists")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Artist artist = snapshot.getValue(Artist.class);
                            String artistName = artist.getArtistName();
                            String artistId = artist.getArtistId();
                            if(!artistsMap.containsKey(artistName)){
                                artistsMap.put(artistId, artistName);
//                                Log.v(LOG_TAG, "fetchArtists " + artistName);
                            }
                        }
                        fetchSongs();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }



    private void fetchSongs() {
        FirebaseDatabase.getInstance().getReference().child("songs")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

//                        String songArtistId = dataSnapshot.getKey();
//                        Log.e("SONGS DB" ,"song artist ID: " + songArtistId);
//                        Log.e("SONGS DB" ,"Artist's songs number: " + dataSnapshot.getChildrenCount());

                        for (DataSnapshot artistIDSnapshot: dataSnapshot.getChildren()) {
                            String songArtistId = artistIDSnapshot.getKey();

//                            Log.e("SONGS DB", "ARTIST ID: " + songArtistId);
                            for (DataSnapshot songSnapshot : artistIDSnapshot.getChildren()) {

                                Song song = songSnapshot.getValue(Song.class);
                                String songTitle = song.getSongTitle();
//                                Log.d("SONGS DB", "songTitle: " + songTitle);

                                if (songsMap.containsKey(songArtistId)) {
                                    ArrayList<String> songsList = songsMap.get(songArtistId);
                                    songsList.add(songTitle);
                                } else {
                                    ArrayList<String> songs = new ArrayList<>();
                                    songs.add(songTitle);
//                                    Log.d(LOG_TAG, "artist Name in songs: " + artistsMap.get(songArtistId));
                                    songsMap.put(artistsMap.get(songArtistId), songs);
//                                    Log.v("SONGS DB", "New song: " + songTitle);
                                }
                            }
                        }
                        Log.e(LOG_TAG, "fetch songs finished");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
 }

