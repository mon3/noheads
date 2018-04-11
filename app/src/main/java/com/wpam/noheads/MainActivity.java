package com.wpam.noheads;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Dictionary;

public class MainActivity extends AppCompatActivity {

    EditText editTextArtist;
    EditText editTextSong;
    Button buttonAdd;
    Spinner spinnerLanguages;

    DatabaseReference databaseArtists;
    DatabaseReference databaseSongs;

    Context mContext;

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

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSong();
            }
        });
    }

        private void addSong(){
            final String artistName = editTextArtist.getText().toString().trim();
            String songTitle = editTextSong.getText().toString().trim();
            String language = spinnerLanguages.getSelectedItem().toString();

            if(!TextUtils.isEmpty(artistName) && !TextUtils.isEmpty(songTitle)){

                String songId = databaseSongs.push().getKey();
                final String artistId = databaseArtists.push().getKey();

                Song song = new Song(songId, songTitle, language);

                databaseArtists.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean artistExists = false;
                        for (DataSnapshot data: dataSnapshot.getChildren()) {
                            String dbArtist = (String)data.child("artistName").getValue();
                            Log.v("Data: ", dbArtist);
                            if (dbArtist.equals(artistName)) {
                                artistExists = true;
                                Log.v("MainActivity", "Artist exists!");
                                Toast.makeText(mContext, "Artist already exists!", Toast.LENGTH_LONG).show();
                                break;

                            }
                        }
                        if (!artistExists){
                            String artistId = databaseArtists.push().getKey();
                            Artist artist = new Artist(artistId, artistName);
                            databaseArtists.child(artistId).setValue(artist);
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

