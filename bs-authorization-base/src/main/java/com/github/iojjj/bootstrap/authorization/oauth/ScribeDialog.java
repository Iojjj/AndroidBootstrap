package com.github.iojjj.bootstrap.authorization.oauth;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.authorization.base.R;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2Authorization;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.oauth.OAuthService;

import java.io.IOException;

/**
 * Implementation of {@link Dialog} that performs an OAuth authorization.
 *
 * @since 1.0
 */
class ScribeDialog extends Dialog {

    private final OAuthService mService;
    private final BSOAuthListener mAuthListener;
    private final Delegate mDelegate;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private OAuth1RequestToken mOAuth1RequestToken;
    private BSOAuthAccessToken mToken;
    private boolean mCanceled;
    private AsyncTask mOAuth1ReqTokenTask;
    private AsyncTask mOAuth1AccTokenTask;
    private AsyncTask mOAuth2AccTokenTask;

    ScribeDialog(@NonNull Context context,
                 @NonNull OAuthService service,
                 @NonNull BSOAuthListener authListener,
                 @NonNull Delegate delegate) {
        super(context);
        BSAssertions.assertNotNull(service, "service");
        BSAssertions.assertNotNull(authListener, "authListener");
        BSAssertions.assertNotNull(delegate, "delegate");
        mService = service;
        mAuthListener = authListener;
        mDelegate = delegate;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Window window = getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_auth_oauth_dialog);
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        mWebView = (WebView) findViewById(R.id.web_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mWebView.setWebChromeClient(new ScribeWebChromeClient());
        mWebView.setWebViewClient(new ScribeWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        setupCookies();
        setOnShowListener(dialog -> requestToken());
        setOnCancelListener(dialog -> mCanceled = true);
        setOnDismissListener(dialog -> {
            // cancel all tasks
            if (mOAuth1ReqTokenTask != null) {
                mOAuth1ReqTokenTask.cancel(true);
            }
            if (mOAuth1AccTokenTask != null) {
                mOAuth1AccTokenTask.cancel(true);
            }
            if (mOAuth2AccTokenTask != null) {
                mOAuth2AccTokenTask.cancel(true);
            }
            if (mToken != null) {
                mAuthListener.onTokenAcquired(mToken);
            } else if (mCanceled) {
                mAuthListener.onCanceled();
            }
        });
    }

    @Override
    public void cancel() {
        // this method must be overridden to handle fragment rotations
        mCanceled = true;
        mDelegate.dismiss();
    }

    /**
     * Request an access token from server.
     */
    private void requestToken() {
        if (mService instanceof OAuth10aService) {
            final OAuth10aService service = (OAuth10aService) mService;
            if (mOAuth1ReqTokenTask != null) {
                mOAuth1ReqTokenTask.cancel(true);
            }
            mOAuth1ReqTokenTask = new AsyncTask<Void, Void, OAuth1RequestToken>() {

                @Nullable
                @Override
                protected OAuth1RequestToken doInBackground(Void... params) {
                    OAuth1RequestToken token = null;
                    try {
                        token = service.getRequestToken();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return token;
                }

                @Override
                protected void onPostExecute(OAuth1RequestToken token) {
                    super.onPostExecute(token);
                    if (token != null) {
                        mOAuth1RequestToken = token;
                        mWebView.loadUrl(service.getAuthorizationUrl(token));
                    } else {
                        mDelegate.dismiss();
                        mAuthListener.onError(new Exception("Can't acquire a request token."));
                    }
                }
            }.execute();
        } else if (mService instanceof OAuth20Service) {
            mWebView.loadUrl(((OAuth20Service) mService).getAuthorizationUrl());
        }
    }

    /**
     * Retrieve an access token from OAuth 2.0 server.
     *
     * @param service instance of OAuth20Service
     * @param code    OAuth 2.0 code required for retrieving of an access token
     */
    private void retrieveAccessToken(@NonNull OAuth20Service service, @NonNull String code) {
        if (mOAuth2AccTokenTask != null) {
            mOAuth2AccTokenTask.cancel(true);
        }
        mOAuth2AccTokenTask = new AsyncTask<Void, Void, OAuth2AccessToken>() {

            @Nullable
            @Override
            protected OAuth2AccessToken doInBackground(Void... params) {
                try {
                    return service.getAccessToken(code);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(OAuth2AccessToken oAuth2AccessToken) {
                super.onPostExecute(oAuth2AccessToken);
                if (oAuth2AccessToken != null) {
                    mToken = BSOAuthAccessToken.wrap(oAuth2AccessToken);
                    mDelegate.dismiss();
                } else {
                    mDelegate.dismiss();
                    mAuthListener.onError(new Exception("Can't retrieve an access token."));
                }
            }
        }.execute();
    }

    /**
     * Retrieve an access token from OAuth 1.0 server.
     *
     * @param service  instance of OAuth10aService
     * @param verifier OAuth 1.0a verifier required for retrieving of an access token
     */
    private void retrieveAccessToken(@NonNull OAuth10aService service, @NonNull String verifier) {
        if (mOAuth1AccTokenTask != null) {
            mOAuth1AccTokenTask.cancel(true);
        }
        mOAuth1AccTokenTask = new AsyncTask<Void, Void, OAuth1AccessToken>() {

            @Nullable
            @Override
            protected OAuth1AccessToken doInBackground(Void... params) {
                try {
                    return service.getAccessToken(mOAuth1RequestToken, verifier);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(OAuth1AccessToken oAuth1AccessToken) {
                super.onPostExecute(oAuth1AccessToken);
                if (oAuth1AccessToken != null) {
                    mToken = BSOAuthAccessToken.wrap(oAuth1AccessToken);
                    mDelegate.dismiss();
                } else {
                    mDelegate.dismiss();
                    mAuthListener.onError(new Exception("Can't retrieve an access token."));
                }
            }
        }.execute();
    }

    /**
     * Clear cookies so users will be prompted to enter a login and a password again.
     */
    private void setupCookies() {
        final CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(value -> {

            });
        } else {
            //noinspection deprecation
            cookieManager.removeAllCookie();
        }
    }

    /**
     * DialogFragment delegate. It's used for handling state restoration (we can't dismiss a dialog
     * directly, only using a DialogFragment's dismiss method).
     */
    interface Delegate {

        void dismiss();
    }

    /**
     * Implementation of WebViewClient that checks for OAuth callbacks and performs retrieving of an
     * access token.
     */
    private class ScribeWebViewClient extends WebViewClient {
        private boolean mErrorOccurred;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            if (url.startsWith(mService.getConfig().getCallback())) {
                if (mService instanceof OAuth10aService) {
                    final OAuth10aService service = (OAuth10aService) mService;
                    final String verifier = extractVerifier(url, OAuthConstants.VERIFIER);
                    final String problem = extractVerifier(url, "oauth_problem");
                    if (!TextUtils.isEmpty(verifier)) {
                        retrieveAccessToken(service, verifier);
                    } else if (!TextUtils.isEmpty(problem) && TextUtils.equals(problem, "user_refused")) {
                        ScribeDialog.this.cancel();
                    } else {
                        mDelegate.dismiss();
                        mAuthListener.onError(new Exception("Can't retrieve a verifier."));
                    }
                }
                if (mService instanceof OAuth20Service) {
                    final OAuth20Service service = (OAuth20Service) mService;
                    final OAuth2Authorization authorization = service.extractAuthorization(url);
                    final String code = authorization.getCode();
                    if (!TextUtils.isEmpty(code)) {
                        retrieveAccessToken(service, code);
                    } else {
                        ScribeDialog.this.cancel();
                        mAuthListener.onError(new Exception("Can't retrieve a verifier."));
                    }
                }
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!mErrorOccurred) {
                mProgressBar.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mErrorOccurred = true;
            mWebView.setVisibility(View.GONE);
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            mErrorOccurred = true;
            mWebView.setVisibility(View.GONE);
        }

        @Nullable
        private String extractVerifier(String redirectLocation, String value) {
            for (String param : redirectLocation.substring(redirectLocation.indexOf('?') + 1).split("&")) {
                final String[] keyValue = param.split("=");
                if (keyValue.length == 2 && TextUtils.equals(keyValue[0], value)) {
                    return keyValue[1];
                }
            }
            return null;
        }
    }

    private class ScribeWebChromeClient extends WebChromeClient {

    }
}
