package com.github.iojjj.bootstrap.support.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Android lifecycle interface.
 *
 * @since 1.0
 */
interface AndroidLifecycle {
    /**
     * Called in {@link AppCompatActivity#onResume()} or {@link Fragment#onResume()}.
     */
    void onResume();

    /**
     * Called in {@link AppCompatActivity#onPause()} or {@link Fragment#onPause()}.
     */
    void onPause();

    /**
     * Called when it's time to save instance state.
     *
     * @param outState instance of Bundle
     */
    void onSaveInstanceState(@NonNull Bundle outState);

    /**
     * Called when it's time to restore instance state.
     *
     * @param savedInstanceState instance of Bundle or null
     */
    void onRestoreInstanceState(@Nullable Bundle savedInstanceState);
}
