package com.wpam.noheads.Push_notifications;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
/**
 * Created by monikas on 30.04.18.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    private static final String LOG_TAG = "MyFirebaseInstanceId";
//    @Override
////    public void onTokenRefresh() {
//        Log.d(LOG_TAG, "onTokenRefresh: ");
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser == null || currentUser.isAnonymous()) {
//            return;
//        }
//
//        savePushToken(refreshedToken, currentUser.getUid());
//    }
}
