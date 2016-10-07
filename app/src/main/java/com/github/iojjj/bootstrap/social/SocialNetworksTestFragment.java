package com.github.iojjj.bootstrap.social;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.iojjj.bootstrap.R;
import com.github.iojjj.bootstrap.authorization.BSGoogleSignInManager;
import com.github.iojjj.bootstrap.core.ui.fragments.BSProgressDialogFragment;
import com.github.iojjj.bootstrap.mvp.BSAbstractFragmentMvpView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment for testing Social Networks integration.
 */
public class SocialNetworksTestFragment extends BSAbstractFragmentMvpView<SocialPresenter>
        implements BSGoogleSignInManager.Callback, SocialView {

    private static final int RC_GOOGLE_PLAY_SERVICES = 1;

    private BSGoogleSignInManager mGoogleSignInManager;
    private BSProgressDialogFragment.Manager mProgressDialogManager;

    public static SocialNetworksTestFragment newInstance() {
        Bundle args = new Bundle();
        SocialNetworksTestFragment fragment = new SocialNetworksTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGoogleSignInManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleSignInManager = BSGoogleSignInManager.Builder.newInstance(getContext())
                .setServerClientId("455940207678-1a0t348gc3pajph6i8cc7unpt8hd57a1.apps.googleusercontent.com")
                .setScopes(new Scope(Scopes.PLUS_LOGIN), new Scope(Scopes.PROFILE))
                .setCallback(this)
                .setRequestCode(1234)
                .build();
        mProgressDialogManager = BSProgressDialogFragment.newInstance().getManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_networks_test, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleSignInManager.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleSignInManager.onStop();
    }

    @Override
    public void onCanceled() {
        Toast.makeText(getContext(), "Sign In Canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        GoogleApiAvailability.getInstance()
                .showErrorDialogFragment(getActivity(), connectionResult.getErrorCode(), RC_GOOGLE_PLAY_SERVICES);
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        Toast.makeText(getContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPresenter(1, this, () -> new SocialPresenterImpl(System.currentTimeMillis()));
    }

    @Override
    public void onSignInFinished() {
        mProgressDialogManager.hideProgressDialog();
    }

    @Override
    public void onSignInStarted() {
        mProgressDialogManager.showProgressDialog("Signing in...");
    }

    @Override
    public void onSignedIn(@NonNull String accessToken) {
        Toast.makeText(getContext(), "Signed In with Google+", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.btn_google, R.id.btn_sign_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_google:
                mGoogleSignInManager.signIn(this);
                break;
            case R.id.btn_sign_out:
                mGoogleSignInManager.signOut();
                break;
        }
    }

    @Override
    public void sowToast() {
        Toast.makeText(getContext(), "Toast", Toast.LENGTH_SHORT).show();
    }
}
