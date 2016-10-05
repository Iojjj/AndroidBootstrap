package com.github.iojjj.bootstrap.authorization.oauth;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.scribejava.core.builder.api.BaseApi;

/**
 * Implementation of a builder that prepares an instance of {@link Bundle} that will be passed for
 * creation of a new instance of DialogFragment.
 *
 * @since 1.0
 */
public class BSOAuthBuilder {

    private String mApiKey;
    private String mApiSecret;
    private String mCallback;
    private int mConnectTimeout;
    private BSOAuthApiGetterAbstract mOAuthApiGetter;
    private int mReadTimeout;
    private String mScope;

    @NonNull
    public Bundle build() {
        BSAssertions.assertNotEmpty(mApiKey, "apiKey");
        BSAssertions.assertNotEmpty(mApiSecret, "apiSecret");
        BSAssertions.assertNotNull(mOAuthApiGetter, "oAuthApiGetter");
        BSAssertions.assertNotNull(mCallback, "callback");
        BSAssertions.assertNotNull(mScope, "scope");
        BSAssertions.assertTrue(mConnectTimeout >= 0, "Parameter \"connectTimeout\" can't hold a negative value.");
        BSAssertions.assertTrue(mReadTimeout >= 0, "Parameter \"readTimeout\" can't hold a negative value.");
        final Bundle bundle = new Bundle();
        bundle.putString(ScribeDelegate.EXTRA_API_KEY, mApiKey);
        bundle.putString(ScribeDelegate.EXTRA_API_SECRET, mApiSecret);
        bundle.putString(ScribeDelegate.EXTRA_CALLBACK, mCallback);
        bundle.putString(ScribeDelegate.EXTRA_SCOPE, mScope);
        bundle.putInt(ScribeDelegate.EXTRA_CONNECT_TIMEOUT, mConnectTimeout);
        bundle.putInt(ScribeDelegate.EXTRA_READ_TIMEOUT, mReadTimeout);
        bundle.putParcelable(ScribeDelegate.EXTRA_API_GETTER, mOAuthApiGetter);
        return bundle;
    }

    /**
     * Set an API key (or an application id).
     *
     * @param apiKey non-empty API key
     */
    public BSOAuthBuilder setApiKey(@NonNull String apiKey) {
        mApiKey = apiKey;
        return this;
    }

    /**
     * Set an API secret key.
     *
     * @param apiSecret non-empty secret key
     */
    public BSOAuthBuilder setApiSecret(@NonNull String apiSecret) {
        mApiSecret = apiSecret;
        return this;
    }

    /**
     * Set a callback URL that will be called when an access token is retrieved.
     *
     * @param callback non-empty callback URL
     */
    public BSOAuthBuilder setCallback(@NonNull String callback) {
        mCallback = callback;
        return this;
    }

    /**
     * Set a connection timeout.
     *
     * @param connectTimeout non-negative value in milliseconds
     */
    public BSOAuthBuilder setConnectTimeout(int connectTimeout) {
        mConnectTimeout = connectTimeout;
        return this;
    }

    /**
     * Set an instance of {@link BSOAuthApiGetterAbstract} that will create a new instance of {@link BaseApi}.
     *
     * @param oAuthApiGetter instance of BSOAuthApiGetterAbstract
     */
    public BSOAuthBuilder setOAuthApiGetter(@NonNull BSOAuthApiGetterAbstract oAuthApiGetter) {
        mOAuthApiGetter = oAuthApiGetter;
        return this;
    }

    /**
     * Set a read timeout.
     *
     * @param readTimeout non-negative value in milliseconds
     */
    public BSOAuthBuilder setReadTimeout(int readTimeout) {
        mReadTimeout = readTimeout;
        return this;
    }

    /**
     * Set scope for an OAuth authorization flow.
     *
     * @param scope non-empty scope
     */
    public BSOAuthBuilder setScope(@NonNull String scope) {
        mScope = scope;
        return this;
    }
}
