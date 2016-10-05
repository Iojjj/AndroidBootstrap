package com.github.iojjj.bootstrap.authorization.oauth;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.cg.BSConstantGenerator;

/**
 * An implementation of {@link DialogFragment} that allows to make both OAuth 1.0a and 2.0
 * authorization flow. It uses <a href="https://github.com/scribejava/scribejava">Scribe Java library</a>
 * for making OAuth requests.
 *
 * @since 1.0
 */
public class BSOAuthDialogFragment extends DialogFragment implements ScribeDialog.Delegate {

    private static final String EXTRA_SCRIBE_BUNDLE = BSConstantGenerator.extra("scribe_bundle");
    @Nullable
    private BSOAuthListener mAuthListener;
    private ScribeDelegate mScribeDelegate;

    /**
     * Create a new instance of DialogFragment.
     *
     * @param scribeBundle a bundle filled with {@link BSOAuthBuilder}
     *
     * @return a new instance of DialogFragment
     */
    public static BSOAuthDialogFragment newInstance(@NonNull Bundle scribeBundle) {
        BSAssertions.assertNotNull(scribeBundle, "scribeBundle");
        Bundle args = new Bundle();
        args.putBundle(EXTRA_SCRIBE_BUNDLE, scribeBundle);
        BSOAuthDialogFragment fragment = new BSOAuthDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments().getBundle(EXTRA_SCRIBE_BUNDLE);
        BSAssertions.assertNotNull(bundle, "scribeBundle");
        final BSOAuthListener authListener = new BSOAuthListener() {
            @Override
            public void onCanceled() {
                if (mAuthListener != null) {
                    mAuthListener.onCanceled();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (mAuthListener != null) {
                    mAuthListener.onError(e);
                }
            }

            @Override
            public void onTokenAcquired(@NonNull BSOAuthAccessToken accessToken) {
                if (mAuthListener != null) {
                    mAuthListener.onTokenAcquired(accessToken);
                }
            }
        };
        mScribeDelegate = new ScribeDelegate(bundle, authListener, this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mScribeDelegate.onCreateDialog(getActivity());
    }

    /**
     * Set an instance of {@link BSOAuthListener} to receive OAuth callbacks.
     *
     * @param authListener instance of BSOAuthListener
     */
    public void setAuthListener(@Nullable BSOAuthListener authListener) {
        mAuthListener = authListener;
    }
}
