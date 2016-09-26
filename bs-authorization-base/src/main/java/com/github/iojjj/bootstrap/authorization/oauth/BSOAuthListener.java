package com.github.iojjj.bootstrap.authorization.oauth;

import android.support.annotation.NonNull;

/**
 * OAuth authorization listener interface.
 *
 * @since 1.0
 */
public interface BSOAuthListener {

    /**
     * Called when an access token is acquired.
     *
     * @param accessToken instance of an access token
     */
    void onTokenAcquired(@NonNull BSOAuthAccessToken accessToken);

    /**
     * Called when an OAuth authorization flow is canceled.
     */
    void onCanceled();

    /**
     * Called when an error occurred during an OAuth authorization flow.
     *
     * @param e instance of Throwable
     */
    void onError(@NonNull Throwable e);
}
