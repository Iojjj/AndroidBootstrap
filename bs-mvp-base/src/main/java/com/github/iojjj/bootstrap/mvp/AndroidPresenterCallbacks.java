package com.github.iojjj.bootstrap.mvp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Android presenter callbacks interface.
 *
 * @since 1.0
 */
interface AndroidPresenterCallbacks {

    /**
     * Called in {@link Activity#onPause()} or {@link Fragment#onPause()}.
     */
    void onPause();

    /**
     * Called when it's time to restore instance state.
     *
     * @param savedInstanceState instance of Bundle or null
     */
    void onRestoreInstanceState(@Nullable Bundle savedInstanceState);

    /**
     * Called in {@link Activity#onResume()} or {@link Fragment#onResume()}.
     */
    void onResume();

    /**
     * Called when it's time to save instance state.
     *
     * @param outState instance of Bundle
     */
    void onSaveInstanceState(@NonNull Bundle outState);
}
