package iojjj.androidbootstrap.ui.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import iojjj.androidbootstrap.R;


@EFragment
public class SimpleWebViewFragment extends Fragment {

    @ViewById(resName = "progress")
    ProgressBar progressBar;
    @ViewById(resName = "web_view")
    WebView webView;

    public static Fragment instance(String url) {
        Fragment fragment = new SimpleWebViewFragment_();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CookieManager.getInstance().setAcceptCookie(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @AfterViews
    public void initiateUI() {
        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
            webSettings.setPluginState(WebSettings.PluginState.ON);
        if (getArguments() != null)
            webView.loadUrl(getArguments().getString("url"));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            if (webView != null)
                webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            if (webView != null)
                webView.onPause();
    }

    private void runJs(String js) {
        if (webView == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(js, null);
        } else {
            webView.loadUrl("javascript:" + js);
        }
    }

    public class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}
