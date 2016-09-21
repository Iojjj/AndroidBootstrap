package com.github.iojjj.bootstrap.authorization;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Manager that allows to retrieve Google Plus access token.
 */
public interface BSGoogleSignInManager extends GoogleSignInManager {

    // TODO: 22.09.2016 documentation
    void signIn(@NonNull Activity activity);

    void signIn(@NonNull Fragment fragment);

    class Builder extends AbstractManagerBuilder<BSGoogleSignInManager> {

        public static Builder newInstance(@NonNull Context context) {
            return new Builder(context);
        }

        private Builder(@NonNull Context context) {
            super(context);
        }

        @Override
        public BSGoogleSignInManager build() {
            return new BSGoogleSignInManagerImpl(this);
        }
    }
}
