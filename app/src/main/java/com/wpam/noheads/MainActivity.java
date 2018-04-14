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

import java.util.Map;


public class MainActivity extends AppCompatActivity {

    EditText editTextArtist;
    EditText editTextSong;
    Button buttonAdd;
    Spinner spinnerLanguages;

    DatabaseReference databaseArtists;
    DatabaseReference databaseSongs;

    Context mContext;


//    Map vill be used to store values


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

//        databaseArtists.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Map<String, String> map = (Map<String, String>)dataSnapshot.getValue();
////                 need to extract values
//                String artistId = map.get("artistId");
//                String artistName = map.get("artistName");
//                Log.v("Artist Id", artistId);
//                Log.v("Artist name", artistName);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


//        databaseSongs.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Map<String, String> map = (Map<String, String>)dataSnapshot.getValue();
////                 need to extract values
//                String songId = map.get("songId");
//                String songTitle = map.get("songTitle");
//                Log.v("song Id", songId);
//                Log.v("Song title", songTitle);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


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

            if(!TextUtils.isEmpty(artistName) && !TextUtils.isEmpty(songTitle)){

                String songId = databaseSongs.push().getKey();
//                final String artistId = databaseArtists.push().getKey();

//                Song song = new Song(songId, songTitle, language);

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
                            databaseSongs = databaseSongs.child(artistId);
                            String songId = databaseSongs.push().getKey();
                            Song song = new Song(songId, songTitle, language);

                            databaseSongs.setValue(song);
                            Artist artist = new Artist(artistId, artistName);
                            DatabaseReference newArtistRef = databaseArtists.push();
                            newArtistRef.setValue(artist);

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


    }

