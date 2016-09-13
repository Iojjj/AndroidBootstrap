package com.github.iojjj.bootstrap.authorization;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;

public interface BSGoogleSignInCoreManager {

    void onStart();

    void onStop();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void signOut();

    interface Callback {

        void onConnectionFailed(@NonNull ConnectionResult connectionResult);

        void onCanceled();

        void onError(@NonNull Throwable throwable);

        void onSignedIn(@NonNull String accessToken);

        void showProgressDialog();

        void hideProgressDialog();
    }

}
