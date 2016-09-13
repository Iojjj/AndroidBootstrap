package com.github.iojjj.bootstrap.authorization;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.iojjj.bootstrap.core.BSAssertions;
import com.google.android.gms.common.api.Scope;

public abstract class ManagerBuilder<T extends BSGoogleSignInCoreManager> {

    @NonNull
    private final Context mContext;
    private String mServerClientId;
    @Nullable
    private BSGoogleSignInCoreManager.Callback mCallback;
    private Scope[] mScopes;
    private int mRequestCode;

    protected ManagerBuilder(@NonNull Context context) {
        BSAssertions.assertNotNull(context, "context");
        mContext = context;
    }

    public ManagerBuilder<T> setServerClientId(@NonNull String serverClientId) {
        mServerClientId = serverClientId;
        return this;
    }

    public ManagerBuilder<T> setCallback(@Nullable BSGoogleSignInCoreManager.Callback callback) {
        mCallback = callback;
        return this;
    }

    public ManagerBuilder<T> setScopes(@NonNull Scope first, Scope... otherScopes) {
        mScopes = new Scope[otherScopes.length + 1];
        mScopes[0] = first;
        System.arraycopy(otherScopes, 0, mScopes, 1, mScopes.length);
        return this;
    }

    public ManagerBuilder<T> setRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }

    public abstract T build();

    @NonNull
    public Context getContext() {
        return mContext;
    }

    public String getServerClientId() {
        return mServerClientId;
    }

    @Nullable
    public BSGoogleSignInCoreManager.Callback getCallback() {
        return mCallback;
    }

    public Scope[] getScopes() {
        return mScopes;
    }

    public int getRequestCode() {
        return mRequestCode;
    }
}
