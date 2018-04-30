package com.wpam.noheads;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by monikas on 29.04.18.
 */

public class StartGameActivity extends AppCompatActivity {

    Button buttonStartGame;
    Button buttonQuickGame;
    Button buttonInvitePlayers;
    Button buttonShowInvitations;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        buttonStartGame = findViewById(R.id.buttonStartGame);
        buttonQuickGame = findViewById(R.id.buttonQuickGame);
        buttonInvitePlayers = findViewById(R.id.buttonInvitePlayers);
        buttonShowInvitations = findViewById(R.id.buttonShowInvitations);


        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent addSongIntent = new Intent(mContext, AddSongActivity.class);

                Log.v("BUTTON: ", "Start button clicked");
//                startActivity(addSongIntent);

            }
        });


        buttonQuickGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("BUTTON: ", "Quick Game button clicked");

            }
        });

        buttonInvitePlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("BUTTON: ", "Invite Players button clicked");
            }
        });

        buttonShowInvitations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("BUTTON: ", "Show Invitations button clicked");
            }
        });

    }


    private static final int RC_SELECT_PLAYERS = 9006;
//
//    private void invitePlayers() {
//        // launch the player selection screen
//        // minimum: 1 other player; maximum: 3 other players
//        Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
//                .getSelectOpponentsIntent(1, 3, true)
//                .addOnSuccessListener(new OnSuccessListener<Intent>() {
//                    @Override
//                    public void onSuccess(Intent intent) {
//                        startActivityForResult(intent, RC_SELECT_PLAYERS);
//                    }
//                });


}

