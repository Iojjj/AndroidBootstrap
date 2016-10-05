package com.github.iojjj.bootstrap.authorization;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.google.android.gms.common.api.Scope;

/**
 * Abstract implementation of {@link ManagerBuilder}.
 *
 * @param <T> type of {@link GoogleSignInManager}
 *
 * @since 1.0
 */
abstract class AbstractManagerBuilder<T extends GoogleSignInManager> implements ManagerBuilder<T> {

    @NonNull
    private final Context mContext;
    @Nullable
    private GoogleSignInManager.Callback mCallback;
    private int mRequestCode;
    private Scope[] mScopes;
    private String mServerClientId;

    AbstractManagerBuilder(@NonNull Context context) {
        BSAssertions.assertNotNull(context, "context");
        mContext = context;
    }

    @Override
    public ManagerBuilder<T> setCallback(@Nullable GoogleSignInManager.Callback callback) {
        mCallback = callback;
        return this;
    }

    @Override
    public ManagerBuilder<T> setRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }

    @Override
    public ManagerBuilder<T> setScopes(@NonNull Scope first, Scope... otherScopes) {
        mScopes = new Scope[otherScopes.length + 1];
        mScopes[0] = first;
        System.arraycopy(otherScopes, 0, mScopes, 1, otherScopes.length);
        return this;
    }

    @Override
    public ManagerBuilder<T> setServerClientId(@NonNull String serverClientId) {
        mServerClientId = serverClientId;
        return this;
    }

    @Nullable
    GoogleSignInManager.Callback getCallback() {
        return mCallback;
    }

    @NonNull
    Context getContext() {
        return mContext;
    }

    int getRequestCode() {
        return mRequestCode;
    }

    Scope[] getScopes() {
        return mScopes;
    }

    String getServerClientId() {
        return mServerClientId;
    }
}
