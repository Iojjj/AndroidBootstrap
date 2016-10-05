package com.github.iojjj.bootstrap.authorization.oauth;

import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;

/**
 * Implementation of an OAuth access token.
 *
 * @since 1.0
 */
public class BSOAuthAccessToken {

    private long mExpiresIn;
    private String mRefreshToken;
    private String mScope;
    private String mToken;
    private String mTokenSecret;

    /**
     * Get an expiration period of an access token. Available only for OAuth 2.0 access token.
     *
     * @return expiration period of an access token in seconds
     */
    public long getExpiresIn() {
        return mExpiresIn;
    }

    /**
     * Get a refresh token for this access token. Available only for OAuth 2.0 access token.
     *
     * @return refresh token for this access token
     */
    public String getRefreshToken() {
        return mRefreshToken;
    }

    /**
     * Get a scope corresponding to this access token. Available only for OAuth 2.0 access token.
     *
     * @return scope corresponding to this access token
     */
    public String getScope() {
        return mScope;
    }

    /**
     * Get an access token string.
     *
     * @return access token string
     */
    public String getToken() {
        return mToken;
    }

    /**
     * Get a token secret for this access token. Available only for OAuth 1.0a access token.
     *
     * @return token secret for this access token
     */
    public String getTokenSecret() {
        return mTokenSecret;
    }

    static BSOAuthAccessToken wrap(@NonNull OAuth2AccessToken token) {
        BSAssertions.assertNotNull(token, "token");
        final BSOAuthAccessToken t = new BSOAuthAccessToken();
        t.mToken = token.getAccessToken();
        t.mExpiresIn = token.getExpiresIn();
        t.mScope = token.getScope();
        t.mRefreshToken = token.getRefreshToken();
        return t;
    }

    static BSOAuthAccessToken wrap(@NonNull OAuth1AccessToken token) {
        BSAssertions.assertNotNull(token, "token");
        final BSOAuthAccessToken t = new BSOAuthAccessToken();
        t.mToken = token.getToken();
        t.mTokenSecret = token.getTokenSecret();
        return t;
    }
}
