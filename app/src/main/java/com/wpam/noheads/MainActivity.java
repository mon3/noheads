package com.wpam.noheads;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText editTextArtist;
    EditText editTextSong;
    Button buttonAdd;
    Spinner spinnerLanguages;

    DatabaseReference databaseArtists;
    DatabaseReference databaseSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            String artistName = editTextArtist.getText().toString().trim();
            String songTitle = editTextSong.getText().toString().trim();
            String language = spinnerLanguages.getSelectedItem().toString();

            if(!TextUtils.isEmpty(artistName) && !TextUtils.isEmpty(songTitle)){
                String artistId = databaseArtists.push().getKey();
                String songId = databaseSongs.push().getKey();
                Artist artist = new Artist(artistId, artistName);
                Song song = new Song(songId, songTitle, language);

                databaseArtists.child(artistId).setValue(artist);
                databaseSongs.child(songId).setValue(song);

                Toast.makeText(this, "Song added to database", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Please enter proper data", Toast.LENGTH_LONG).show();
            }
    }


    }

