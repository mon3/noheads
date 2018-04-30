package com.wpam.noheads;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddSongActivity extends AppCompatActivity {
    EditText editTextArtist;
    EditText editTextSong;
    Button buttonAdd;
    Spinner spinnerLanguages;

    DatabaseReference databaseArtists;
    DatabaseReference databaseSongs;

    Context mContext;


    Map<String, String> artistsMap = new HashMap<>();
    Map<String, ArrayList<String>> songsMap = new HashMap<>(); // key = artistID, songsMap = list of songs


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_add_song);

        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
        databaseSongs = FirebaseDatabase.getInstance().getReference("songs");

        editTextArtist = findViewById(R.id.editTextName);
        editTextSong = findViewById(R.id.editTextSong);
        buttonAdd = findViewById(R.id.buttonAddSong);
        spinnerLanguages = findViewById(R.id.spinnerLanguages);
        Intent intent = getIntent();
//        artistsMap = (HashMap<String, String>)intent.getSerializableExtra("artistsData");
//        songsMap = (Map<String, ArrayList<String>>)intent.getSerializableExtra("songsData");

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
                Log.e("SONGS DB" ,"Artist's songs number: " + dataSnapshot.getChildrenCount());

                for (DataSnapshot songSnapshot: dataSnapshot.getChildren()) {
                    Song song = songSnapshot.getValue(Song.class);
                    String songTitle =  song.getSongTitle();
                    Log.d("SONGS DB",songTitle );

                    if (songsMap.containsKey(songArtistId)){
                        ArrayList<String> songsList = songsMap.get(songArtistId);
                        songsList.add(songTitle);
                    }
                    else {
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
                Log.e("SONGS DB" ,"Song list removed" + songArtistId);

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
                Log.v("BUTTON: ", "button clicked");
                addSongArtist();
            }
        });
    }

    private void addSongArtist(){
        final String artistName = editTextArtist.getText().toString().trim();
        final String songTitle = editTextSong.getText().toString().trim();
        final String language = spinnerLanguages.getSelectedItem().toString();

        if(!TextUtils.isEmpty(artistName) && !TextUtils.isEmpty(songTitle)){
            Log.v("BUTTON: ", "before event listener");
            databaseArtists.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean artistExists = false;
                    Log.v("BUTTON: ", "inside ddatabase artists event listener");
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String dbArtist = (String) data.child("artistName").getValue();
                        Log.v("Data: ", dbArtist);
                        if (dbArtist.equals(artistName)) {
                            artistExists = true;
                            String existingArtistId = data.child("artistId").getValue().toString();
                            Log.v("DB ARTIST: ", dbArtist);
                            if (songsMap.containsKey(existingArtistId)) {
                                ArrayList<String> artistSongs = songsMap.get(existingArtistId);
                                if (artistSongs.contains(songTitle)) {
                                    Log.d("Song exists: ", songTitle);
                                    Toast.makeText(mContext, "Song already exists!", Toast.LENGTH_LONG).show();
                                } else {
                                    Log.v("ARTIST: ", "Artist - yes, song - no");
                                    Log.d("New song: ", songTitle);
                                    addNewSongForArtist(existingArtistId, songTitle, language);
                                }
                            }

                        }


                    }

                    if(!artistExists) {
                        Log.v("ARTIST: ", "!artist Exists");
                        String artistId = databaseArtists.push().getKey();

                        Artist artist = new Artist(artistId, artistName);
                        DatabaseReference newArtistRef = databaseArtists.push();
                        newArtistRef.setValue(artist);

                        addNewSongForArtist(artistId, songTitle, language);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("Firebase", databaseError.getMessage());
                }
            });

        }
        else {
            Toast.makeText(this, "Please enter proper data", Toast.LENGTH_LONG).show();
        }
    }


    private void addNewSongForArtist (String artistId, String songTitle, String language) {

        DatabaseReference newSongRef = FirebaseDatabase.getInstance().getReference("songs").child(artistId);

        String songId = newSongRef.push().getKey();
        Song song = new Song(songTitle, language);
        newSongRef.child(songId).setValue(song);
        Toast.makeText(mContext, "Song added to database", Toast.LENGTH_LONG).show();
    }


}

