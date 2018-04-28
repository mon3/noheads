package com.wpam.noheads;

import android.content.Context;
import android.database.DatabaseUtils;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    EditText editTextArtist;
    EditText editTextSong;
    Button buttonAdd;
    Spinner spinnerLanguages;

    DatabaseReference databaseArtists;
    DatabaseReference databaseSongs;

    Context mContext;


    Map<String, String> artistsMap = new HashMap<>();
    Map<String, ArrayList<String>> songsMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
        databaseSongs = FirebaseDatabase.getInstance().getReference("songs");

        editTextArtist = findViewById(R.id.editTextName);
        editTextSong = findViewById(R.id.editTextSong);
        buttonAdd = findViewById(R.id.buttonAddSong);
        spinnerLanguages = findViewById(R.id.spinnerLanguages);


        databaseArtists.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Artist artist = dataSnapshot.getValue(Artist.class);
                String artistName = artist.getArtistName();
                String artistId = artist.getArtistId();

                artistsMap.put(artistName, artistId);
                Log.v("Artist NAME: ", artistName);
                Log.v("Artist ID: ", artistId);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Artist artist = dataSnapshot.getValue(Artist.class);
                artistsMap.remove(artist);
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
                Song song = dataSnapshot.getValue(Song.class);
                String songTitle = song.getSongTitle();
                String artistId = dataSnapshot.getKey(); // parent ID = artist ID

                if (songsMap.containsKey(songTitle)){
                    Log.v("database songs: ", "contains songTitle!");
                    ArrayList<String> songs = songsMap.get(songTitle);
                    songs.add(songTitle);
                }
                else {
                    Log.v("database songs: ", "cnew song");
                    ArrayList<String> songs = new ArrayList<String>();
                    songs.add(songTitle);
                    songsMap.put(artistId, songs);
                }
//                Log.v("SONG TITLE: ", songTitle);
//                Log.v("SONG Artist ID: ", artistId);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Song song = dataSnapshot.getValue(Song.class);
//                String songTitle = song.getSongTitle();
                songsMap.remove(song);
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
                addSongArtist();
            }
        });
    }

        private void addSongArtist(){
            final String artistName = editTextArtist.getText().toString().trim();
            final String songTitle = editTextSong.getText().toString().trim();
            final String language = spinnerLanguages.getSelectedItem().toString();
            Log.v("Language selected: ", language);
            Log.v("To add song: ", songTitle);

            if(!TextUtils.isEmpty(artistName) && !TextUtils.isEmpty(songTitle)){

                databaseArtists.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean artistExists = false;
                        for (DataSnapshot data: dataSnapshot.getChildren()) {
                            String dbArtist = (String)data.child("artistName").getValue();
                            Log.v("Data: ", dbArtist);
                            if (dbArtist.equals(artistName)) {
                                artistExists = true;
                                String existingArtistId = data.child("artistId").getValue().toString();
                                Log.v("DB ARTIST: ", dbArtist);
                                if (songsMap.containsKey(existingArtistId))
                                {
                                    ArrayList<String> artistSongs = songsMap.get(existingArtistId);
                                    if (artistSongs.contains(songTitle))
                                    {
                                        Log.d("Song exists: ", songTitle);
                                    }
                                    else {
                                        Log.d("New song: ", songTitle);
                                        addNewSongForArtist(existingArtistId, songTitle, language);
                                    }
                                }


                                else {
                                    databaseSongs = databaseSongs.child(existingArtistId);
                                    String songId = databaseSongs.push().getKey();
                                    Song song = new Song(songTitle, language);
                                    databaseSongs.child(songId).setValue(song);
                                    Toast.makeText(mContext, "Song added to database", Toast.LENGTH_LONG).show();
                                    Log.v("Song addition: ", "Song added in else!!!");
                                }

                                DatabaseUtil.findSongByArtistId(existingArtistId, songTitle, new DatabaseUtil.Listener() {
                                    @Override
                                    public void onSongRetrieved(Song song) {
                                        Log.v("Song from UTIL: ", song.getSongTitle());
                                    }
                                });

//                                DatabaseReference artistSongsRef = databaseSongs.child(existingArtistId).getRef().orderByChild("songTitle").equalTo(songTitle);
//                                artistSongsRef.addChildEventListener(ChildEventListener childListener)
//                                Query artistSongSnap = databaseSongs.child(existingArtistId).orderByKey();
//                                Log.v("Children: ", artistSongSnap.toString());


//                                for (DataSnapshot song: databaseSongs.child(existingArtistId).)

                                Log.v("Existing artidst ID: ", existingArtistId);
                                Log.v("MainActivity", "Artist exists!");
                                Toast.makeText(mContext, "Artist already exists!", Toast.LENGTH_LONG).show();
                                break;

                            }
                        }
                        if (!artistExists){
                            String artistId = databaseArtists.push().getKey();

                            Artist artist = new Artist(artistId, artistName);
                            DatabaseReference newArtistRef = databaseArtists.push();
                            newArtistRef.setValue(artist);

                            databaseSongs = databaseSongs.child(artistId);
                            String songId = databaseSongs.push().getKey();
                            Song song = new Song(songTitle, language);

//                            databaseSongs.setValue(song);
                            databaseSongs.child(songId).setValue(song);


                            Log.v("MainActivity", "New artist will be added");
                            Toast.makeText(mContext, "Song added to database", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Firebase", databaseError.getMessage());
                        Toast.makeText(mContext, "Song added to database", Toast.LENGTH_LONG).show();
                    }
                });
//                databaseArtists.child(artistId).setValue(artist);
//                databaseSongs.child(songId).setValue(song);
//
            }
            else {
                Toast.makeText(this, "Please enter proper data", Toast.LENGTH_LONG).show();
            }
    }


    private void addNewSongForArtist (String artistId, String songTitle, String language) {

//        ToDO: add song to firebase if not exists
        DatabaseReference newSongRef = FirebaseDatabase.getInstance().getReference("songs").child(artistId);
        String songId = newSongRef.push().getKey();
        Song song = new Song(songTitle, language);
        newSongRef.child(songId).setValue(song);
    }

    private void readArtists () {
//      ToDO: read artists and songs into the hashmap only once

    }


    }

