package com.github.iojjj.bootstrap.authorization;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;

/**
 * Implementation of BSGoogleSignInManager.
 */
class BSGoogleSignInManagerImpl extends AbstractGoogleSignInManager implements BSGoogleSignInManager {

    private final int mRequestCode;

    BSGoogleSignInManagerImpl(@NonNull AbstractManagerBuilder builder) {
        super(builder);
        mRequestCode = builder.getRequestCode();
    }

    @Override
    public void signIn(@NonNull Activity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(getGoogleApiClient());
        activity.startActivityForResult(signInIntent, mRequestCode);
    }

    @Override
    public void signIn(@NonNull Fragment fragment) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(getGoogleApiClient());
        fragment.startActivityForResult(signInIntent, mRequestCode);
    }
}
