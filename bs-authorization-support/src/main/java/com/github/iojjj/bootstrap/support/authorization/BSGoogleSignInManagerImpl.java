package com.github.iojjj.bootstrap.support.authorization;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.github.iojjj.bootstrap.authorization.ManagerBuilder;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;

import java.io.IOException;

/**
 * Implementation of BSGoogleSignInManager.
 */
class BSGoogleSignInManagerImpl implements BSGoogleSignInManager, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static int sRcSignIn = 1234;
    private final Context mContext;
    private final GoogleApiClient mGoogleApiClient;
    private final Scope[] mScopes;
    private Handler mHandler;

    BSGoogleSignInManagerImpl(@NonNull ManagerBuilder builder) {
        mContext = builder.getContext().getApplicationContext();
        mHandler = new CallbackHandler(builder.getCallback());
        if (builder.getRequestCode() != 0) {
            sRcSignIn = builder.getRequestCode();
        }
        final GoogleSignInOptions.Builder gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(builder.getServerClientId());
        final Scope[] builderScopes = builder.getScopes();
        if (builderScopes != null && builderScopes.length > 0) {
            mScopes = new Scope[builderScopes.length];
            System.arraycopy(builderScopes, 0, mScopes, 0, mScopes.length);
            Scope first = builderScopes[0];
            Scope[] other = new Scope[builderScopes.length - 1];
            System.arraycopy(builderScopes, 1, other, 0, other.length);
            gsoBuilder.requestScopes(first, other);
        } else {
            mScopes = new Scope[0];
        }
        final GoogleSignInOptions gso = gsoBuilder
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(builder.getContext().getApplicationContext())
                .addApi(Plus.API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void signIn(@NonNull Activity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, sRcSignIn);
    }

    @Override
    public void signIn(@NonNull android.support.v4.app.Fragment fragment) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        fragment.startActivityForResult(signInIntent, sRcSignIn);
    }

    @Override
    public void signOut() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == sRcSignIn) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acc = result.getSignInAccount();
                if (acc != null) {
                    final String idToken = acc.getIdToken();
                    final String email = acc.getEmail();
                    if (TextUtils.isEmpty(idToken) || TextUtils.isEmpty(idToken.trim())) {
                        mHandler.obtainMessage(CallbackHandler.RESULT_ERROR, new Exception("Access token is empty")).sendToTarget();
                    } else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(email.trim())) {
                        mHandler.obtainMessage(CallbackHandler.RESULT_ERROR, new Exception("Email is empty")).sendToTarget();
                    } else {
                        getAccessToken(email);
                    }
                } else {
                    mHandler.obtainMessage(CallbackHandler.RESULT_ERROR, new Exception("Account is null")).sendToTarget();
                }
            } else {
                final Status status = result.getStatus();
                if (status.getStatusCode() == 12501) {
                    mHandler.sendEmptyMessage(CallbackHandler.RESULT_CANCELED);
                } else {
                    mHandler.obtainMessage(CallbackHandler.RESULT_ERROR, new Exception("" + status.getStatusMessage())).sendToTarget();
                }
            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void getAccessToken(@NonNull String email) {
        mHandler.sendEmptyMessage(CallbackHandler.RESULT_SHOW_PROGRESS_DIALOG);
        new Thread(() -> {
            final Account selectedAccount = new Account(email, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            try {
                final String scopes = TextUtils.join(" ", mScopes);
                final String token = GoogleAuthUtil.getToken(mContext, selectedAccount,
                        "oauth2:" + scopes);
                mHandler.obtainMessage(CallbackHandler.RESULT_ACCESS_TOKEN, token).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.obtainMessage(CallbackHandler.RESULT_ERROR, e).sendToTarget();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
                mHandler.obtainMessage(CallbackHandler.RESULT_ERROR, e).sendToTarget();
            } finally {
                mHandler.sendEmptyMessage(CallbackHandler.RESULT_HIDE_PROGRESS_DIALOG);
            }
        }).start();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mHandler.obtainMessage(CallbackHandler.RESULT_CONNECTION_FAILED, connectionResult).sendToTarget();
    }

    private static class CallbackHandler extends Handler {

        private static final int RESULT_ACCESS_TOKEN = 1;
        private static final int RESULT_ERROR = 2;
        private static final int RESULT_CANCELED = 3;
        private static final int RESULT_CONNECTION_FAILED = 4;
        private static final int RESULT_SHOW_PROGRESS_DIALOG = 5;
        private static final int RESULT_HIDE_PROGRESS_DIALOG = 6;

        @Nullable
        private final BSGoogleSignInManager.Callback mCallback;

        private CallbackHandler(@Nullable BSGoogleSignInManager.Callback callback) {
            super(Looper.getMainLooper());
            mCallback = callback;
        }

        @Override
        public void handleMessage(Message msg) {
            if (mCallback == null) {
                super.handleMessage(msg);
                return;
            }
            switch (msg.what) {
                case RESULT_ACCESS_TOKEN: {
                    mCallback.onSignedIn((String) msg.obj);
                    break;
                }
                case RESULT_ERROR: {
                    mCallback.onError((Throwable) msg.obj);
                    break;
                }
                case RESULT_CANCELED: {
                    mCallback.onCanceled();
                    break;
                }
                case RESULT_CONNECTION_FAILED: {
                    mCallback.onConnectionFailed((ConnectionResult) msg.obj);
                    break;
                }
                case RESULT_SHOW_PROGRESS_DIALOG: {
                    mCallback.showProgressDialog();
                    break;
                }
                case RESULT_HIDE_PROGRESS_DIALOG: {
                    mCallback.hideProgressDialog();
                    break;
                }
                default: {
                    super.handleMessage(msg);
                    break;
                }
            }
        }
    }
}
