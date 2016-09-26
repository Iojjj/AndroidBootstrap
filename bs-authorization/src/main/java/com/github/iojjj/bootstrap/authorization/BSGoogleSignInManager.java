package com.github.iojjj.bootstrap.authorization;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Sign in manager that allows to retrieve Google Plus access token.
 *
 * @since 1.0
 */
public interface BSGoogleSignInManager extends GoogleSignInManager {

    /**
     * Sign in and receive callback in {@link Activity#onActivityResult(int, int, Intent)} method.
     *
     * @param activity instance of Activity
     */
    void signIn(@NonNull Activity activity);

    /**
     * Sign in and receive callback in {@link Fragment#onActivityResult(int, int, Intent)} method.
     *
     * @param fragment instance of Fragment
     */
    void signIn(@NonNull Fragment fragment);

    /**
     * BSOAuthBuilder that create a new instance of {@link BSGoogleSignInManager}.
     */
    class Builder extends AbstractManagerBuilder<BSGoogleSignInManager> {

        private Builder(@NonNull Context context) {
            super(context);
        }

        /**
         * Create a new instance of BSOAuthBuilder
         *
         * @param context instance of Context
         * @return a new instance of BSOAuthBuilder
         */
        public static Builder newInstance(@NonNull Context context) {
            return new Builder(context);
        }

        @Override
        public BSGoogleSignInManager build() {
            return new BSGoogleSignInManagerImpl(this);
        }
    }
}
