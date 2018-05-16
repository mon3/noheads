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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";

    Button buttonAdd;

    DatabaseReference databaseArtists;
    DatabaseReference databaseSongs;

    Context mContext;


    Map<String, String> artistsMap = new HashMap<>();
    Map<String, ArrayList<String>> songsMap = new HashMap<>(); // key = artistID, songsMap = list of songs

//    private ActivityMainBinding binding;
    private String withId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        buttonAdd = findViewById(R.id.buttonAddDb);

//        Bundle extras = getIntent().getExtras();
////        String type = extras.getString("type");
////        if (type.equals("com.wpam.noheads.wifi")) {
//            withId = extras.getString("withId");
////            binding.canvas.setWifiWith(withId);
//            String gameId = extras.getString("gameId");
////            binding.canvas.setGameId(gameId);
////            binding.canvas.setMe(extras.getString("me"));
//
//            FirebaseDatabase.getInstance().getReference().child("games")
//                    .child(gameId)
//                    .setValue(null);


//        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
////        fetchArtists();
//        databaseSongs = FirebaseDatabase.getInstance().getReference("songs");

//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create channel to show notifications.
//            String channelId  = getString(R.string.default_notification_channel_id);
//            String channelName = getString(R.string.default_notification_channel_name);
//            NotificationManager notificationManager =
//                    getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
//                    channelName, NotificationManager.IMPORTANCE_LOW));
//        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
//        if (getIntent().getExtras() != null) {
//            for (String key : getIntent().getExtras().keySet()) {
//                Object value = getIntent().getExtras().get(key);
//                Log.d("NOTIFICATION", "Key: " + key + " Value: " + value);
//            }
//        }




        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent addSongIntent = new Intent(mContext, AddSongActivity.class);
                Intent addSongIntent = new Intent(mContext, LoginActivity.class);
//                addSongIntent.putExtra("artistsData", (Serializable) artistsMap);
//                addSongIntent.putExtra("songsData", (Serializable) songsMap);
                Log.v("BUTTON: ", "button clicked");
                startActivity(addSongIntent);

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
                                artistsMap.put(artistName, artistId);
                                Log.v(LOG_TAG, "fetchArtists " + artistName);
                            }
                        }
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
                        String songArtistId = dataSnapshot.getKey();
                        Log.e(LOG_TAG, "Artist's songs number: " + dataSnapshot.getChildrenCount());

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
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
 }

