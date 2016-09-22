package com.github.iojjj.bootstrap.authorization;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;

/**
 * Google sign in manager interface.
 *
 * @since 1.0
 */
interface GoogleSignInManager {

    /**
     * Should be called in {@link Activity#onStart()} or {@link Fragment#onStart()} methods.
     */
    void onStart();

    /**
     * Should be called in {@link Activity#onStop()} or {@link Fragment#onStop()} methods.
     */
    void onStop();

    /**
     * Should be called in {@link Activity#onActivityResult(int, int, Intent)} or
     * {@link Fragment#onActivityResult(int, int, Intent)} methods.
     *
     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * Sign out from Google account.
     */
    void signOut();

    /**
     * Callbacks interface.
     */
    interface Callback {

        /**
         * Called when connection to Google Play Services failed.
         *
         * @param connectionResult instance of ConnectionResult
         */
        void onConnectionFailed(@NonNull ConnectionResult connectionResult);

        /**
         * Called when connection to Google Play Services canceled.
         */
        void onCanceled();

        /**
         * Called when there is an error occurred.
         *
         * @param throwable instance of Throwable
         */
        void onError(@NonNull Throwable throwable);

        /**
         * Called when user successfully signed into it's Google account.
         *
         * @param accessToken access token
         */
        void onSignedIn(@NonNull String accessToken);

        /**
         * Called when a sign in operation started.
         */
        void onSignInStarted();

        /**
         * Called when a sign in operation finished.
         */
        void onSignInFinished();
    }

}
