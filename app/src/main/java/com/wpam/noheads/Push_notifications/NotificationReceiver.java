package com.wpam.noheads.Push_notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wpam.noheads.MainActivity;
import com.wpam.noheads.Model.User;
import com.wpam.noheads.NewGameActivity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.wpam.noheads.Util.getCurrentUserId;
import static com.wpam.noheads.wifi.Holder.FIREBASE_CLOUD_FUNCTIONS_BASE;

public class NotificationReceiver  extends BroadcastReceiver {
        private static final String LOG_TAG = "NotificationReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "onReceive: " + intent.getAction());

            FirebaseDatabase.getInstance().getReference().child("users")
                    .child(getCurrentUserId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User me = dataSnapshot.getValue(User.class);

                            OkHttpClient client = new OkHttpClient();

                            String to = intent.getExtras().getString("to");
                            String withId = intent.getExtras().getString("withId");
                            Log.d(LOG_TAG, to);
                            Log.d(LOG_TAG, intent.getAction().toString());
                            String format = String
                                    .format("%s/sendNotification?to=%s&fromPushId=%s&fromId=%s&fromName=%s&type=%s",
                                            FIREBASE_CLOUD_FUNCTIONS_BASE,
                                            to,
                                            me.getPushId(),
                                            getCurrentUserId(),
                                            me.getName(),
                                            intent.getAction());

                            Log.d(LOG_TAG, "onDataChange: " + format);
                            Request request = new Request.Builder()
                                    .url(format)
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                }
                            });

//                            otrzymał accept od zaproszonej osoby
                            if (intent.getAction().equals("accept")) {
                                String gameId = withId + "-" + getCurrentUserId();
                                Time today = new Time(Time.getCurrentTimezone());
                                today.setToNow();
                                FirebaseDatabase.getInstance().getReference().child("games")
                                        .child(gameId).child("1")
                                        .setValue(0);

//                                        .setValue(today.format("%k_%M_%S"));

                                Intent newGameIntent = new Intent(context, NewGameActivity.class)
                                        .putExtra("type", "wifi")
                                        .putExtra("me", "0")
                                        .putExtra("gameId", gameId)
                                        .putExtra("withId", withId);
                                newGameIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                context.startActivity(newGameIntent);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }


}



