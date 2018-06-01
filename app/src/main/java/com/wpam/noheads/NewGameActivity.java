package com.wpam.noheads;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import static android.content.Intent.getIntent;
import static android.databinding.DataBindingUtil.setContentView;

public class NewGameActivity extends AppCompatActivity{


    private static final String LOG_TAG = "NewGameActivity";
    private String withId;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_new_game);
        Bundle extras = getIntent().getExtras();
        String type = extras.getString("type");
        Log.d(LOG_TAG, "game type: " + type);
        if (type.equals("wifi")) {
            withId = extras.getString("withId");
//            binding.canvas.setWifiWith(withId);
            String gameId = extras.getString("gameId");
//            binding.canvas.setGameId(gameId);
//            binding.canvas.setMe(extras.getString("me"));
            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            FirebaseDatabase.getInstance().getReference().child("games")
                    .child(gameId).child("restart")
                    .setValue(today.format("%k:%M:%S"));

        }
    }



}
