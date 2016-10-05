package com.github.iojjj.bootstrap.authorization;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.Scope;

/**
 * Interface of builder that creates a new instance of {@link GoogleSignInManager}.
 *
 * @param <T> type of {@link GoogleSignInManager}
 *
 * @since 1.0
 */
public interface ManagerBuilder<T extends GoogleSignInManager> {

    /**
     * Create a new instance of {@link GoogleSignInManager}.
     *
     * @return a new instance of GoogleSignInManager
     */
    T build();

    /**
     * Set callback listener.
     *
     * @param callback instance of Callback
     */
    ManagerBuilder<T> setCallback(@Nullable GoogleSignInManager.Callback callback);

    /**
     * Set request code for operation. Required field.
     *
     * @param requestCode any non-zero request code
     */
    ManagerBuilder<T> setRequestCode(int requestCode);

    /**
     * Set scopes that should be granted during sign in flow.
     *
     * @param first       first scope
     * @param otherScopes other scopes
     */
    ManagerBuilder<T> setScopes(@NonNull Scope first, Scope... otherScopes);

    /**
     * Set server client id. Required field.
     *
     * @param serverClientId server client id
     */
    ManagerBuilder<T> setServerClientId(@NonNull String serverClientId);
}
