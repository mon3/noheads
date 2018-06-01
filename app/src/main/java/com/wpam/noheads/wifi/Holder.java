package com.wpam.noheads.wifi;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wpam.noheads.Model.User;
import com.wpam.noheads.databinding.UserListItemBinding;

import android.databinding.DataBindingUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.wpam.noheads.Util.getCurrentUserId;


public class Holder extends RecyclerView.ViewHolder {
    public UserListItemBinding binding;
    public static final String FIREBASE_CLOUD_FUNCTIONS_BASE = "https://us-central1-noheads-7fa10.cloudfunctions.net";
    private static final String LOG_TAG = "FirebaseIDService";


    public Holder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        binding.invite.setOnClickListener(v -> {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.child("users")
                    .child(getCurrentUserId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User me = dataSnapshot.getValue(User.class);

                            OkHttpClient client = new OkHttpClient();

                            String to = binding.getUser().getName();
                            String toPushId = UserListActivity.usersMap.get(to);
                            Log.i(LOG_TAG, "message to: " + to);
                            Log.i(LOG_TAG, "message toPushID: " + toPushId);

                            Log.i(LOG_TAG, "message from: " +me.getPushId() + " " +  getCurrentUserId());

                            Request request = new Request.Builder()
                                    .url(String
                                            .format("%s/sendNotification?to=%s&fromPushId=%s&fromId=%s&fromName=%s&type=%s",
                                                    FIREBASE_CLOUD_FUNCTIONS_BASE,
                                                    toPushId,
                                                    me.getPushId(),
                                                    getCurrentUserId(),
                                                    me.getName(),
                                                    "invite"))
                                    .build();
                            Log.d("Holder", "Request sent!");
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        });
    }
}