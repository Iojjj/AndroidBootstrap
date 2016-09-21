package com.github.iojjj.bootstrap.authorization;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.google.android.gms.common.api.Scope;

// TODO: 22.09.2016 documentation
abstract class AbstractManagerBuilder<T extends GoogleSignInManager> implements ManagerBuilder<T> {

    @NonNull
    private final Context mContext;
    private String mServerClientId;
    @Nullable
    private GoogleSignInManager.Callback mCallback;
    private Scope[] mScopes;
    private int mRequestCode;

    AbstractManagerBuilder(@NonNull Context context) {
        BSAssertions.assertNotNull(context, "context");
        mContext = context;
    }

    @Override
    public ManagerBuilder<T> setServerClientId(@NonNull String serverClientId) {
        mServerClientId = serverClientId;
        return this;
    }

    @Override
    public ManagerBuilder<T> setCallback(@Nullable GoogleSignInManager.Callback callback) {
        mCallback = callback;
        return this;
    }

    @Override
    public ManagerBuilder<T> setScopes(@NonNull Scope first, Scope... otherScopes) {
        mScopes = new Scope[otherScopes.length + 1];
        mScopes[0] = first;
        System.arraycopy(otherScopes, 0, mScopes, 1, mScopes.length);
        return this;
    }

    @Override
    public ManagerBuilder<T> setRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }

    @NonNull
    Context getContext() {
        return mContext;
    }

    String getServerClientId() {
        return mServerClientId;
    }

    @Nullable
    GoogleSignInManager.Callback getCallback() {
        return mCallback;
    }

    Scope[] getScopes() {
        return mScopes;
    }

    int getRequestCode() {
        return mRequestCode;
    }
}
