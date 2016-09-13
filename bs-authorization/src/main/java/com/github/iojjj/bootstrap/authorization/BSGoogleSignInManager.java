package com.github.iojjj.bootstrap.authorization;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.core.BSAssertions;

/**
 * Manager that allows to retrieve Google Plus access token.
 */
public interface BSGoogleSignInManager extends BSGoogleSignInCoreManager {

    void signIn(@NonNull Activity activity);

    void signIn(@NonNull Fragment fragment);

    class Factory {

        public static ManagerBuilder<BSGoogleSignInManager> newBuilder(@NonNull Context context) {
            BSAssertions.assertNotNull(context, "context");
            return new Builder(context);
        }

        private Factory() {
            //no instance
        }

        private static class Builder extends ManagerBuilder<BSGoogleSignInManager> {

            Builder(@NonNull Context context) {
                super(context);
            }

            @Override
            public BSGoogleSignInManager build() {
                return new BSGoogleSignInManagerImpl(this);
            }
        }
    }
}
