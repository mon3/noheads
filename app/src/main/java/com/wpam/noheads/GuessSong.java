package com.wpam.noheads;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

public class GuessSong {
    private boolean isMyTurn = true;
    private boolean done;
    private boolean isWIfi;
    private String otherUserId;
    private String gameId;


    private static final String LOG_TAG = "GuessSong";

    public void setWifiWith(String withId) {
        isWIfi = true;
        otherUserId = withId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
        Log.d(LOG_TAG, "setGameId: " + gameId);
        FirebaseDatabase.getInstance().getReference().child("games")
                .child(gameId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getValue() == null) {
                            return;
                        }
                        String key = dataSnapshot.getKey();
                        if (!key.equals("restart")) {
//                            int row = Integer.parseInt(key.substring(0, 1));
//                            int col = Integer.parseInt(key.substring(2, 3));
//                            Integer shape = dataSnapshot.getValue(Integer.class);
//
//                            if (field[row][col] == NONE) {
//                                drawShape(row, col, shape);
//                            }
                        } else {
                            restart();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getValue() == null) {
                            return;
                        }
                        if (dataSnapshot.getKey().equals("restart")) {
                            restart();

                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void restart() {

        if (!isWIfi) {
            isMyTurn = true;
        } else {
//            isMyTurn = MY_SHAPE == Constants.X;
        }
        done = false;
//        ai = new AI(MY_SHAPE, AI_SHAPE, field);
//        background.setImageResource(R.drawable.chalkboard2);
//        background.setBackground(null);
//        invalidate();
    }

    public void setMe(String me) {
        if (me.equals("1")) {
            isMyTurn = true;
//            Toast.makeText(getContext(), "Your turn", Toast.LENGTH_SHORT);
        } else {
            isMyTurn = false;
//            Toast.makeText(getContext(), "Opponent's turn", Toast.LENGTH_SHORT);
        }
    }
}
