package com.wpam.noheads;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wpam.noheads.Model.Artist;
import com.wpam.noheads.Model.Song;
import com.wpam.noheads.Push_notifications.FirebaseMessaging;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.content.Intent.getIntent;
import static android.databinding.DataBindingUtil.setContentView;

public class NewGameActivity extends AppCompatActivity{


    private static final String LOG_TAG = "NewGameActivity";
    private String withId;
    Context mContext;
    private boolean isMyTurn = true;
    private boolean done;
    private boolean isWIfi;
    private String otherUserId;
    private String gameId;
    private String me;
    private TextView guessSongTV;
    private TextView guessArtistTV;

    Button buttonReset;
    Button buttonWrong;
    Button buttonRight;
    String rival;
    Map<String, String> artistsMap = new HashMap<>(); // key = artist ID, value = Artist Name
    Map<String, ArrayList<String>> songsMap = new HashMap<>(); // key = artistID, songsMap = list of songs

    ProgressBar timeLeftPrograssBar;
    CountDownTimer countTimer;
    int i=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_new_game);
        guessSongTV = findViewById(R.id.textViewSongTitle);
        guessArtistTV = findViewById(R.id.textViewArtist);

        buttonRight = findViewById(R.id.buttonTrue);
        buttonWrong = findViewById(R.id.buttonFalse);
        buttonReset = findViewById(R.id.buttonRestartGame);
        timeLeftPrograssBar = findViewById(R.id.timeProgressBar);
        Bundle extras = getIntent().getExtras();
        String type = extras.getString("type");

        // ToDO: change to 120000
        countTimer=new CountDownTimer(30000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                i++;
                timeLeftPrograssBar.setProgress((int)i*100/(30));
            }

            @Override
            public void onFinish() {
                i = 0;
                countTimer.cancel();
                timeLeftPrograssBar.setProgress(0);
                FirebaseDatabase.getInstance().getReference().child("games")
                        .child(gameId).child(me)
                        .setValue(null);
                FirebaseDatabase.getInstance().getReference().child("games")
                        .child(gameId).child(rival)
                        .setValue(0);
            }
        };
        Log.e(LOG_TAG, "Artists map length: " + artistsMap.size());
        Log.e(LOG_TAG, "Songs map length: " + songsMap.size());

        Log.d(LOG_TAG, "game type: " + type);
        if (type.equals("wifi")) {
            withId = extras.getString("withId");
            String gameId = extras.getString("gameId");
            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            Log.d(LOG_TAG, "me from extras: " + extras.get("me"));
            String me = extras.getString("me");

            setMe(me);
            setWifiWith(withId);
            fetchArtists(gameId);

            if (me.equals("1"))
            {
                rival = "0";
            }
            else {
                rival = "1";
            }
            buttonWrong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeLeftPrograssBar.setProgress(0);
                    countTimer.onFinish();

                }
            });
// ToDO: zmienic TIMER!
            buttonRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeLeftPrograssBar.setProgress(0);
                    countTimer.onFinish();
                }
            });

            buttonReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i = 0;
                    timeLeftPrograssBar.setProgress(0);
                    FirebaseDatabase.getInstance().getReference().child("games")
                            .child(gameId).setValue(null);
                    finish();
                }
            });

        }
    }


    @Override
    public void onStop() {
        super.onStop();
        i = 0;
        if (countTimer!= null) {
            countTimer.cancel();
        }
        timeLeftPrograssBar.setProgress(0);
        FirebaseDatabase.getInstance().getReference().child("games").child(gameId).setValue(null);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (countTimer!= null) {
            countTimer.cancel();
        }


    }


    @Override
    public void onResume(){
        super.onResume();
        countTimer=new CountDownTimer(30000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                i++;
                timeLeftPrograssBar.setProgress((int)i*100/(30));

            }

            @Override
            public void onFinish() {
                i = 0;
                countTimer.cancel();
                timeLeftPrograssBar.setProgress(0);
                FirebaseDatabase.getInstance().getReference().child("games")
                        .child(gameId).child(me)
                        .setValue(null);
                FirebaseDatabase.getInstance().getReference().child("games")
                        .child(gameId).child(rival)
                        .setValue(0);
            }
        };

    }


    public void setWifiWith(String withId) {
        isWIfi = true;
        otherUserId = withId;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void setGameId(String gameId) {
        this.gameId = gameId;
        Log.d(LOG_TAG, "setGameId: " + gameId);
        FirebaseDatabase.getInstance().getReference().child("games")
                .child(gameId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getValue() == null) {
//                            return;
                            finish();
                        }
                        String key = dataSnapshot.getKey();
                        Log.d(LOG_TAG, "data snapshot key: " + key);
                        Log.d(LOG_TAG, "me: " + me);
                        if (!key.equals("restart") && key.equals(me)) {

                            Random generator = new Random();
                            Object[] keys = songsMap.keySet().toArray();
                            String randomArtist = (String)keys[generator.nextInt(keys.length)];
                            ArrayList<String> artistSongs = songsMap.get(randomArtist);
                            String randomSong = artistSongs.get((new Random()).nextInt(artistSongs.size()));;
                            Log.d(LOG_TAG, "RANDOM ARTIST " + randomArtist);
                            Log.d(LOG_TAG, "RANDOM SONG " + randomSong);
                            guessSongTV.setText(randomSong);
                            guessArtistTV.setText(randomArtist);

                            guessSongTV.setVisibility(View.VISIBLE);
                            guessArtistTV.setVisibility(View.VISIBLE);

                            buttonReset.setClickable(true);
                            buttonRight.setClickable(true);
                            buttonWrong.setClickable(true);
                            timeLeftPrograssBar.setProgress(0);
                            timeLeftPrograssBar.setVisibility(View.VISIBLE);
                            countTimer.start();
                        }
                        else if (!key.equals("restart") && !key.equals(me)){
                            guessSongTV.setVisibility(View.INVISIBLE);
                            guessArtistTV.setVisibility(View.INVISIBLE);
                            buttonReset.setClickable(false);
                            buttonRight.setClickable(false);
                            buttonWrong.setClickable(false);
                            timeLeftPrograssBar.setProgress(0);
                            timeLeftPrograssBar.setVisibility(View.INVISIBLE);
                        }
                        else if ( FirebaseDatabase.getInstance().getReference().child("games")
                                .child(gameId).equals("restart")) {
                            FirebaseDatabase.getInstance().getReference().child("games")
                                    .child(gameId).setValue(null);
                            finish();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getValue() == null) {
                            return;
                        }
                        else {
                            Log.d(LOG_TAG, " on ChildChanged - count children: " + dataSnapshot.getChildrenCount());
                        }

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d(LOG_TAG, "children number" + dataSnapshot.getChildrenCount());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }



    public void setMe(String gotMe) {
        me = gotMe;
        if (gotMe.equals("1")) {
            isMyTurn = true;
        } else {
            isMyTurn = false;
        }
    }

    private void fetchArtists(String gameId) {
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
                            }
                        }
                        Log.d(LOG_TAG, "fetch artists finished!");
                        fetchSongs(gameId);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void fetchSongs(String gameId) {
        FirebaseDatabase.getInstance().getReference().child("songs")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot artistIDSnapshot: dataSnapshot.getChildren()) {
                            String songArtistId = artistIDSnapshot.getKey();

                            for (DataSnapshot songSnapshot : artistIDSnapshot.getChildren()) {

                                Song song = songSnapshot.getValue(Song.class);
                                String songTitle = song.getSongTitle();

                                if (songsMap.containsKey(songArtistId)) {
                                    ArrayList<String> songsList = songsMap.get(songArtistId);
                                    songsList.add(songTitle);
                                } else {
                                    ArrayList<String> songs = new ArrayList<>();
                                    songs.add(songTitle);
                                    songsMap.put(artistsMap.get(songArtistId), songs);
                                }
                            }
                        }
                        Log.e(LOG_TAG, "fetch songs finished");
                        setGameId(gameId);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


}
