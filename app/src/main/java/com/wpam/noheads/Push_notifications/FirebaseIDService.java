package com.wpam.noheads.Push_notifications;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.wpam.noheads.Util.savePushToken;

/**
 * Created by monikas on 30.04.18.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String LOG_TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        Log.d(LOG_TAG, "onTokenRefresh: ");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(LOG_TAG, "Refreshed token: " + refreshedToken);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putString("token",refreshedToken).commit();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.isAnonymous()) {
            Log.d(LOG_TAG, "INSIDE REFRESH, but ANONYMOUS " + refreshedToken);

            return;
        }
        Log.d(LOG_TAG, "BEFORE SAVE PUSH TOKEN " + refreshedToken);

        savePushToken(refreshedToken, currentUser.getUid());
    }
}
