package com.github.iojjj.bootstrap.authorization.oauth;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.cg.BSConstantGenerator;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuthService;

/**
 * Scribe delegate implementation. This class retrieves all necessary data from bundle and configures
 * an instance of {@link OAuthService}.
 *
 * @since 1.0
 */
class ScribeDelegate {

    static final String EXTRA_API_KEY = BSConstantGenerator.extra("api_key");
    static final String EXTRA_API_SECRET = BSConstantGenerator.extra("api_secret");
    static final String EXTRA_CALLBACK = BSConstantGenerator.extra("callback");
    static final String EXTRA_CONNECT_TIMEOUT = BSConstantGenerator.extra("connect_timeout");
    static final String EXTRA_READ_TIMEOUT = BSConstantGenerator.extra("read_timeout");
    static final String EXTRA_SCOPE = BSConstantGenerator.extra("scope");
    static final String EXTRA_API_GETTER = BSConstantGenerator.extra("oauth_api_getter");

    private final OAuthService mOAuthService;
    private final BSOAuthListener mAuthListener;
    private final ScribeDialog.Delegate mDelegate;

    ScribeDelegate(@NonNull Bundle bundle, @NonNull BSOAuthListener authListener,
                   @NonNull ScribeDialog.Delegate delegate) {
        final String apiKey = bundle.getString(EXTRA_API_KEY);
        final String apiSecret = bundle.getString(EXTRA_API_SECRET);
        final String callback = bundle.getString(EXTRA_CALLBACK);
        final int connectTimeout = bundle.getInt(EXTRA_CONNECT_TIMEOUT);
        final int readTimeout = bundle.getInt(EXTRA_READ_TIMEOUT);
        final String scope = bundle.getString(EXTRA_SCOPE);
        final BSOAuthApiGetterAbstract oAuthApiGetter = bundle.getParcelable(EXTRA_API_GETTER);
        BSAssertions.assertNotNull(oAuthApiGetter, "oAuthApiGetter");
        BSAssertions.assertNotNull(authListener, "mAuthListener");
        BSAssertions.assertNotNull(delegate, "delegate");
        mOAuthService = new ServiceBuilder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback(callback)
                .connectTimeout(connectTimeout)
                .readTimeout(readTimeout)
                .scope(scope)
                .build(oAuthApiGetter.getApi());
        mAuthListener = authListener;
        mDelegate = delegate;
    }

    /**
     * Creates a new dialog that will perform an OAuth authorization.
     *
     * @param context instance of Context
     * @return instance of Dialog
     */
    Dialog onCreateDialog(@NonNull Context context) {
        BSAssertions.assertNotNull(context, "context");
        return new ScribeDialog(context, mOAuthService, mAuthListener, mDelegate);
    }

}
